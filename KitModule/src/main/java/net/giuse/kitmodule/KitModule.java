package net.giuse.kitmodule;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.engine.ProcessEngine;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.kitmodule.databases.kit.KitOperations;
import net.giuse.kitmodule.databases.kit.PlayerKitOperations;
import net.giuse.kitmodule.files.FileManager;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.serializer.KitSerializer;
import net.giuse.kitmodule.serializer.PlayerKitTimeSerializer;
import net.giuse.kitmodule.serializer.serializedobject.KitSerialized;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitTimeSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.DBOperations;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Module Kit
 */
public class KitModule extends Services implements Savable {
    @Getter
    private Cache<UUID, PlayerTimerSystem> cachePlayerKit;
    @Getter
    private Cache<String, KitBuilder> kitElements;
    @Getter
    private final FileManager configManager = new FileManager();
    @Getter
    private final Serializer<KitSerialized> kitBuilderSerializer = new KitSerializer();
    @Inject
    private MainModule mainModule;
    @Getter
    private Serializer<PlayerKitTimeSerialized> playerKitTimeSerializer;
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
        MessageLoaderKit messageLoaderKit = mainModule.getInjector().getSingleton(MessageLoaderKit.class);

        //Initialize Files
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");
        ReflectionsFiles.loadFiles(configManager);
        messageLoaderKit.load();

        //Load Kit
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading SQL...");
        kitElements =  Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        cachePlayerKit = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        messageLoaderKit.load();
        kitOperations.getAllString().forEach(kitElement -> {
            KitSerialized kitSerialized = kitBuilderSerializer.decoder(kitElement);
            kitSerialized.getKitBuilder().build();
            kitElements.put(kitSerialized.getName(), kitSerialized.getKitBuilder());
        });

        //Load PlayerTimeKit
        for (String playerTimeString : playerKitOperations.getAllString()) {
            System.out.println(playerTimeString);
            PlayerKitTimeSerialized playerTimerSystem = playerKitTimeSerializer.decoder(playerTimeString);
            playerTimerSystem.getPlayerTimerSystem().runTaskTimerAsynchronously(mainModule, 20L, 20L);
            cachePlayerKit.put(playerTimerSystem.getUuid(), playerTimerSystem.getPlayerTimerSystem());
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
    @SneakyThrows
    public void save() {
        mainModule.getConnectorSQLite().openConnect();
        playerKitOperations.dropTable();
        playerKitOperations.createTable();
        cachePlayerKit.asMap().forEach(((uuid, playerTimerSystem) -> playerKitOperations.insert(playerKitTimeSerializer.encode(new PlayerKitTimeSerialized(uuid, playerTimerSystem)))));
        kitOperations.dropTable();
        kitOperations.createTable();
        kitElements.asMap().forEach((name, kitBuilder) -> kitOperations.insert(kitBuilderSerializer.encode(new KitSerialized(name, kitBuilder))));
    }


    /**
     * Search PlayerTimerSystem from Set
     */
    public PlayerTimerSystem getPlayerTime(UUID playerUUID) {
        return cachePlayerKit.getIfPresent(playerUUID);
    }

    /**
     * Search Kit  from Name in a Set
     */
    public KitBuilder getKit(String searchKitBuilder) {
        return kitElements.getIfPresent(searchKitBuilder.toLowerCase());
    }

}

