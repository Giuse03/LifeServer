package net.giuse.economymodule;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.economymodule.database.EconomyOperations;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.FileManager;
import net.giuse.economymodule.serializer.EconPlayerSerializer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class EconomyService extends Services implements Savable {
    @Getter
    private final Set<EconPlayer> econPlayers = new HashSet<>();
    private final HashMap<String, String> messages = new HashMap<>();
    private final Serializer<EconPlayer> econPlayerSerializer = new EconPlayerSerializer();
    @Inject
    private MainModule mainModule;
    @Getter
    private EconomyManager customEcoManager;
    private EconomyOperations economyOperations;


    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        customEcoManager = mainModule.getInjector().getSingleton(EconomyManager.class);
        economyOperations = mainModule.getInjector().getSingleton(EconomyOperations.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        economyOperations.createTable();
        for (String econPlayers : economyOperations.getAllString()) {
            this.econPlayers.add(econPlayerSerializer.decoder(econPlayers));
        }
        FileManager configManager;
        ReflectionsFiles.loadFiles(configManager = new FileManager());
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_economy.yml")) == 0) {
            configManager.messagesLoader();
        }
        for (String messageConfig : configManager.getMessagesYaml().getConfigurationSection("messages").getKeys(true)) {
            messages.put(messageConfig, configManager.getMessagesYaml().getString("messages." + messageConfig));
        }
        messages.put("no-perms", mainModule.getConfig().getString("no-perms"));
    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
        save().run();
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Runnable save() {
        return () -> econPlayers.forEach(player -> {
            if (!economyOperations.isPresent(player.getPlayer())) {
                economyOperations.insert(econPlayerSerializer.encode(player));
            } else {
                economyOperations.update(econPlayerSerializer.encode(player));
            }
        });
    }

    public EconPlayer getEconPlayer(String name) {
        return econPlayers.stream().filter(econPlayer -> econPlayer.getPlayer().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.get(key));
    }

}
