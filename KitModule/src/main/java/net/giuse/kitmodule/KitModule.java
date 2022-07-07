package net.giuse.kitmodule;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.kitmodule.databases.kit.KitOperations;
import net.giuse.kitmodule.databases.kit.PlayerKitOperations;
import net.giuse.kitmodule.files.FileManager;
import net.giuse.kitmodule.serializer.KitSerializer;
import net.giuse.kitmodule.serializer.PlayerKitTimeSerializer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.DBOperations;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import org.bukkit.ChatColor;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Module Kit
 */
public class KitModule extends Services implements Savable {
    @Getter
    private final Set<PlayerTimerSystem> playerTimerSystems = new HashSet<>();
    @Getter
    private final Set<KitBuilder> kitElements = new HashSet<>();
    private final HashMap<String, String> messages = new HashMap<>();
    @Getter
    private final FileManager configManager = new FileManager();

    @Getter
    private Serializer<KitBuilder> kitBuilderSerializer;
    @Inject
    private MainModule mainModule;
    @Getter
    private Serializer<PlayerTimerSystem> playerKitTimeSerializer;
    private DBOperations kitOperations, playerKitOperations;


    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        //Initialize Serializer and Databases
        playerKitTimeSerializer = mainModule.getInjector().getSingleton(PlayerKitTimeSerializer.class);
        kitOperations = mainModule.getInjector().getSingleton(KitOperations.class);
        playerKitOperations = mainModule.getInjector().getSingleton(PlayerKitOperations.class);
        kitBuilderSerializer = mainModule.getInjector().getSingleton(KitSerializer.class);
        //Initialize Files
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");
        ReflectionsFiles.loadFiles(configManager);
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_kit.yml")) == 0) {
            configManager.messagesLoader();
        }
        for (String messageConfig : configManager.getMessagesYaml().getConfigurationSection("messages").getKeys(true)) {
            messages.put(messageConfig, configManager.getMessagesYaml().getString("messages." + messageConfig));
        }
        messages.put("no-perms", mainModule.getConfig().getString("no-perms"));

        //Load Kit and PlayerTimeKit

        kitOperations.getAllString().forEach(kitElement -> kitElements.add(kitBuilderSerializer.decoder(kitElement)));
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading SQL...");
        for (String playerTimeString : playerKitOperations.getAllString()) {
            PlayerTimerSystem playerTimerSystem = playerKitTimeSerializer.decoder(playerTimeString);
            playerTimerSystem.runTaskTimerAsynchronously(mainModule, 20L, 20L);
            playerTimerSystems.add(playerTimerSystem);
        }
    }

    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");
        //Saves Kits
        save();
    }

    /**
     * Priority Module
     */
    @Override
    public int priority() {
        return -1;
    }


    /**
     * Save Sets of PlayerTimerSystem and Kit in a Database
     */
    @Override
    public void save() {
        mainModule.getConnectorSQLite().openConnect();
        playerKitOperations.dropTable();
        playerKitOperations.createTable();
        playerTimerSystems.forEach(playerTimeSystem -> playerKitOperations.insert(playerKitTimeSerializer.encode(playerTimeSystem)));
        kitOperations.dropTable();
        kitOperations.createTable();
        kitElements.forEach(kitBuilder -> kitOperations.insert(kitBuilderSerializer.encode(kitBuilder)));
    }

    /**
     * Search PlayerTimerSystem from Set
     */
    public PlayerTimerSystem getPlayerTime(UUID playerUUID) {
        return playerTimerSystems.stream().filter(playerTimerSystem -> playerTimerSystem.getUuid().equals(playerUUID)).findFirst().orElse(null);
    }

    /**
     * Search Kit  from Name in a Set
     */
    public KitBuilder getKit(String searchKitBuilder) {
        return kitElements.stream().filter(kitBuilder -> kitBuilder.getName().equalsIgnoreCase(searchKitBuilder)).findFirst().orElse(null);
    }

    /**
     * Search a Message from HashMap
     */
    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.get(key));
    }


}

