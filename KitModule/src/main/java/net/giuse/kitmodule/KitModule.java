package net.giuse.kitmodule;


import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;
import net.giuse.kitmodule.databases.kit.querykit.LoadQueryKit;
import net.giuse.kitmodule.databases.kit.querykit.SaveKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.LoadPlayerKit;
import net.giuse.kitmodule.databases.kit.queryplayerkit.SavePlayerKit;
import net.giuse.kitmodule.files.ConfigKits;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.kitmodule.messages.serializer.PlayerKitCooldownSerializer;
import net.giuse.kitmodule.messages.serializer.serializedobject.PlayerKitCooldownSerialized;
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
    private ConfigKits fileKits;
    @Getter
    private final Object2ObjectMap<UUID, PlayerKitCooldown> cachePlayerKit = new Object2ObjectArrayMap<>();
    @Getter
    private final Object2ObjectMap<String, KitBuilder> kitElements = new Object2ObjectArrayMap<>();
    @Inject
    private MainModule mainModule;
    @Getter
    private final Serializer<PlayerKitCooldownSerialized> playerCooldownSerializer = new PlayerKitCooldownSerializer();


    /**
     * Load Module Kit
     */
    @SneakyThrows
    @Override
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading Kits...");


        //Initialize Files
        ReflectionsFiles.loadFiles(fileKits = new ConfigKits());
        mainModule.getInjector().getSingleton(MessageLoaderKit.class).load();

        //Load Kit
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Loading SQL...");
        mainModule.getInjector().getSingleton(LoadQueryKit.class).query();

        //Load PlayerTimeKit
        mainModule.getInjector().getSingleton(LoadPlayerKit.class).query();
    }

    /**
     * Unload Module Kit
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eKitModule§9] §7Unloading Kits...");

        //Saves Kits
        mainModule.getInjector().getSingleton(SaveKit.class).query();
        mainModule.getInjector().getSingleton(SavePlayerKit.class).query();
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
    public PlayerKitCooldown getPlayerCooldown(UUID playerUUID) {
        return cachePlayerKit.get(playerUUID);
    }

    /**
     * Search Kit  from Name in a Set
     */
    public KitBuilder getKit(String searchKitBuilder) {
        return kitElements.get(searchKitBuilder.toLowerCase());
    }

}

