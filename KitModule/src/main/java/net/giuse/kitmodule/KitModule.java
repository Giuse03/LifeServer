package net.giuse.kitmodule;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.kitmodule.databases.kit.querykit.LoadQueryKit;
import net.giuse.kitmodule.databases.kit.querykit.SaveQueryKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.LoadQueryPlayerKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.SaveQueryPlayerKit;
import net.giuse.kitmodule.files.FileManager;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.serializer.KitSerializer;
import net.giuse.kitmodule.serializer.PlayerKitTimeSerializer;
import net.giuse.kitmodule.serializer.serializedobject.KitSerialized;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitTimeSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Module Kit
 */
public class KitModule extends Services {
    @Getter
    private final FileManager configManager = new FileManager();
    @Getter
    private final Serializer<KitSerialized> kitBuilderSerializer = new KitSerializer();
    @Getter
    private Cache<UUID, PlayerTimerSystem> cachePlayerKit;
    @Getter
    private Cache<String, KitBuilder> kitElements;
    @Inject
    private MainModule mainModule;
    @Getter
    private Serializer<PlayerKitTimeSerialized> playerKitTimeSerializer;


    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");

        //Initialize Serializer and Databases
        playerKitTimeSerializer = mainModule.getInjector().getSingleton(PlayerKitTimeSerializer.class);

        //Initialize Files
        ReflectionsFiles.loadFiles(configManager);
        mainModule.getInjector().getSingleton(MessageLoaderKit.class).load();

        //Load Kit
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading SQL...");
        kitElements = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        cachePlayerKit = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        mainModule.getInjector().getSingleton(LoadQueryKit.class).query();

        //Load PlayerTimeKit
        mainModule.getInjector().getSingleton(LoadQueryPlayerKit.class).query();
    }

    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");

        //Saves Kits
        mainModule.getInjector().getSingleton(SaveQueryKit.class).query();
        mainModule.getInjector().getSingleton(SaveQueryPlayerKit.class).query();
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

