package net.giuse.economymodule;


import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.economymodule.databases.EconQuery;
import net.giuse.economymodule.databases.SaveQueryEcon;
import net.giuse.economymodule.economymanager.EconomyManager;
import net.giuse.economymodule.files.FileManager;
import net.giuse.economymodule.messageloader.MessageLoaderEconomy;
import net.giuse.economymodule.serializer.EconPlayerSerializer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;


public class EconomyService extends Services {
    @Getter
    private final Serializer<EconPlayerSerialized> econPlayerSerializer = new EconPlayerSerializer();
    @Getter
    private final HashMap<UUID, Double> econPlayersCache = new HashMap<>();
    @Inject
    private MainModule mainModule;
    @Getter
    private EconomyManager customEcoManager;
    @Getter
    private FileManager configManager;

    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Loading economy...");
        customEcoManager = mainModule.getInjector().getSingleton(EconomyManager.class);
        mainModule.getServer().getServicesManager().register(Economy.class, customEcoManager, mainModule, ServicePriority.Normal);
        mainModule.getInjector().getSingleton(EconQuery.class).query();
        ReflectionsFiles.loadFiles(configManager = new FileManager());
        MessageLoaderEconomy messageLoaderEconomy = mainModule.getInjector().getSingleton(MessageLoaderEconomy.class);
        messageLoaderEconomy.load();
    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eEconomy §9] §7Unloading economy...");
        mainModule.getInjector().getSingleton(SaveQueryEcon.class).query();
        ;
    }

    @Override
    public int priority() {
        return 0;
    }


    public double getBalancePlayer(UUID uuid) {
        return econPlayersCache.get(uuid);
    }

    public void setBalance(UUID uuid, double balance) {
        econPlayersCache.put(uuid, balance);
    }

    public boolean getEconPlayerIsPresent(UUID uuid) {
        return econPlayersCache.containsKey(uuid);
    }

}