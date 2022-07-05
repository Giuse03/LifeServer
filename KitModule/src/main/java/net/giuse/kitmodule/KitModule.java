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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KitModule extends Services implements Savable {
    @Getter
    private final Set<PlayerTimerSystem> playerTimerSystems = new HashSet<>();
    @Getter
    private final Set<String> kitElements = new HashSet<>();
    private final HashMap<String, String> messages = new HashMap<>();
    @Getter
    private final FileManager configManager = new FileManager();
    @Getter
    private final Serializer<KitBuilder> kitBuilderSerializer = new KitSerializer();
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


        //Initialize Serializer and Databases
        playerKitTimeSerializer = mainModule.getInjector().getSingleton(PlayerKitTimeSerializer.class);
        kitOperations = mainModule.getInjector().getSingleton(KitOperations.class);
        playerKitOperations = mainModule.getInjector().getSingleton(PlayerKitOperations.class);


        //Save Kit and PlayerTimeKit
        kitOperations.createTable();
        playerKitOperations.createTable();
        kitElements.addAll(kitOperations.getAllString());
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
        save().run();
    }

    /**
     * Priority Module
     */
    @Override
    public int priority() {
        return -1;
    }

    /**
     * Search PlayerTimerSystem from Set
     */
    public PlayerTimerSystem getPlayerTime(UUID playerUUID, KitBuilder kitBuilder) {
        for (PlayerTimerSystem playerTimerSystem : playerTimerSystems) {
            if (playerTimerSystem.getUuid().equals(playerUUID) && playerTimerSystem.getNameKit().equals(kitBuilder.getName()))
                return playerTimerSystem;
        }
        return null;
    }

    /**
     * Search Kit from Set
     */
    public KitBuilder getKit(String searchKitBuilder) {
        for (String kitElement : kitElements) {
            if (searchKitBuilder.equalsIgnoreCase(kitBuilderSerializer.decoder(kitElement).getName())) {
                return kitBuilderSerializer.decoder(kitElement);
            }
        }
        return null;
    }

    /**
     * Search a Message from HashMap
     */
    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.get(key));
    }


    /**
     * Save Sets of PlayerTimerSystem and Kit in a Database
     */
    @Override
    public Runnable save() {
        return () -> {
            mainModule.getConnectorSQLite().openConnect();
            playerTimerSystems.forEach(playerTimeSystem -> {
                if (!playerKitOperations.isPresent(playerKitTimeSerializer.encode(playerTimeSystem)))
                    playerKitOperations.insert(playerKitTimeSerializer.encode(playerTimeSystem));
                else playerKitOperations.update(playerKitTimeSerializer.encode(playerTimeSystem));
            });
            kitElements.forEach(kitElements -> {
                if (!kitOperations.isPresent(kitElements))
                    kitOperations.insert(kitElements);
                else kitOperations.remove(kitElements);
            });
        };
    }


}

