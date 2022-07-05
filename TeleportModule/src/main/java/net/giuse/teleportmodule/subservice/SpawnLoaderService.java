package net.giuse.teleportmodule.subservice;


import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.database.SpawnOperations;
import net.giuse.teleportmodule.serializer.SpawnBuilderSerializer;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SpawnLoaderService extends Services {


    private final Serializer<SpawnBuilder> spawnBuilderSerializer = new SpawnBuilderSerializer();
    @Getter
    @Setter
    private SpawnBuilder spawnBuilder;
    @Inject
    private MainModule mainModule;
    private SpawnOperations operations;

    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Spawn...");
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        operations = mainModule.getInjector().getSingleton(SpawnOperations.class);
        operations.getAllString().forEach(newSpawnBuilder -> spawnBuilder = spawnBuilderSerializer.decoder(newSpawnBuilder));
        operations.dropTable();
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_spawn.yml")) == 0) {
            teleportModule.getFileManager().setMessagesSpawn();
        }
        for (String messageConfig : teleportModule.getFileManager().getMessagesSpawnYaml().getConfigurationSection("messages.spawn").getKeys(true)) {
            teleportModule.getMessage().put(messageConfig, teleportModule.getFileManager().getMessagesSpawnYaml().getString("messages.spawn." + messageConfig));
        }
    }

    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Spawn...");
        operations.createTable();
        if (spawnBuilder != null) {
            operations.insert(spawnBuilderSerializer.encode(spawnBuilder));
        }
    }

    @Override
    public int priority() {
        return 1;
    }

}
