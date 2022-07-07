package net.giuse.teleportmodule.subservice;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.database.SpawnOperations;
import net.giuse.teleportmodule.serializer.SpawnBuilderSerializer;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        spawnOperations = mainModule.getInjector().getSingleton(SpawnOperations.class);

        //Load from database
        spawnOperations.getAllString().forEach(newSpawnBuilder -> spawnBuilder = spawnBuilderSerializer.decoder(newSpawnBuilder));

        //Load File
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_spawn.yml")) == 0) {
            teleportModule.getFileManager().setMessagesSpawn();
        }

        //Load Message
        for (String messageConfig : teleportModule.getFileManager().getMessagesSpawnYaml().getConfigurationSection("messages.spawn").getKeys(true)) {
            teleportModule.getMessage().put(messageConfig, teleportModule.getFileManager().getMessagesSpawnYaml().getString("messages.spawn." + messageConfig));
        }
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
