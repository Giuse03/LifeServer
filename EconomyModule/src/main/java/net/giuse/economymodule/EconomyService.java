package net.giuse.economymodule;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.economymodule.database.EconomyOperations;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.FileManager;
import net.giuse.economymodule.messageloader.MessageLoaderEconomy;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class EconomyService extends Services implements Savable {
    @Getter
    private final Cache<UUID, Double> econPlayers;
    private final Serializer<EconPlayer> econPlayerSerializer = new EconPlayerSerializer();
    @Inject
    private MainModule mainModule;
    @Getter
    private EconomyManager customEcoManager;
    private EconomyOperations economyOperations;
    @Getter
    private FileManager configManager;

    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        econPlayers = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        customEcoManager = mainModule.getInjector().getSingleton(EconomyManager.class);
        economyOperations = mainModule.getInjector().getSingleton(EconomyOperations.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        economyOperations.createTable();
        for (String econPlayers : economyOperations.getAllString()) {
            this.econPlayers.add(econPlayerSerializer.decoder(econPlayers));
        }
        ReflectionsFiles.loadFiles(configManager = new FileManager());
        MessageLoaderEconomy messageLoaderEconomy = mainModule.getInjector().getSingleton(MessageLoaderEconomy.class);
        messageLoaderEconomy.load();
    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
        save();
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void save() {
        econPlayers.forEach(player -> {
            if (!economyOperations.isPresent(player.getPlayer().toString())) {
                economyOperations.insert(econPlayerSerializer.encode(player));
            } else {
                economyOperations.update(econPlayerSerializer.encode(player));
            }
        });
    }

    public EconPlayer getEconPlayer(UUID uuid) {
        return econPlayers.stream().filter(econPlayer -> econPlayer.getPlayer().equals(uuid)).findFirst().orElse(null);
    }

}