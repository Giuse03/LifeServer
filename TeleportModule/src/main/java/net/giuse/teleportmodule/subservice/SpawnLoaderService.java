package net.giuse.teleportmodule.subservice;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.database.spawnquery.SaveQuerySpawn;
import net.giuse.teleportmodule.database.spawnquery.SpawnQuery;
import net.giuse.teleportmodule.serializer.SpawnBuilderSerializer;

import javax.inject.Inject;

public class SpawnLoaderService extends Services {

    @Getter
    private final Serializer<SpawnBuilder> spawnBuilderSerializer = new SpawnBuilderSerializer();
    @Inject
    private MainModule mainModule;
    @Getter
    @Setter
    private SpawnBuilder spawnBuilder;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Spawn...");

        //Load from database
        mainModule.getInjector().getSingleton(SpawnQuery.class).query();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
        mainModule.getInjector().getSingleton(SaveQuerySpawn.class).query();
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }

    /*
     * Save in database
     */
}
