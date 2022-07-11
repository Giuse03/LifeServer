package net.giuse.teleportmodule.subservice;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.database.SpawnOperations;
import net.giuse.teleportmodule.serializer.SpawnBuilderSerializer;

import javax.inject.Inject;

public class SpawnLoaderService extends Services implements Savable {
    private final Serializer<SpawnBuilder> spawnBuilderSerializer = new SpawnBuilderSerializer();
    @Inject
    private MainModule mainModule;
    @Getter
    @Setter
    private SpawnBuilder spawnBuilder;
    private SpawnOperations spawnOperations;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Spawn...");
        spawnOperations = mainModule.getInjector().getSingleton(SpawnOperations.class);

        //Load from database
        spawnOperations.getAllString().forEach(newSpawnBuilder -> spawnBuilder = spawnBuilderSerializer.decoder(newSpawnBuilder));
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
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
    @Override
    public void save() {
        spawnOperations.dropTable();
        spawnOperations.createTable();
        if (spawnBuilder != null) {
            spawnOperations.insert(spawnBuilderSerializer.encode(spawnBuilder));
        }
    }
}
