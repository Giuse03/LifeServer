package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.WarpBuilder;
import net.giuse.teleportmodule.database.WarpOperations;
import net.giuse.teleportmodule.serializer.WarpBuilderSerializer;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class WarpLoaderService extends Services implements Savable {

    private final Serializer<WarpBuilder> warpBuilderSerializer = new WarpBuilderSerializer();
    @Getter
    private final Set<WarpBuilder> warpBuilders = new HashSet<>();
    @Inject
    private MainModule mainModule;
    private WarpOperations warpOperations;

    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_warp.yml")) == 0) {
            teleportModule.getFileManager().messagesWarpLoader();
        }
        for (String messageConfig : teleportModule.getFileManager().getMessagesWarpYaml().getConfigurationSection("messages.warp").getKeys(true)) {
            teleportModule.getMessage().put(messageConfig, teleportModule.getFileManager().getMessagesWarpYaml().getString("messages.warp." + messageConfig));
        }

        warpOperations = mainModule.getInjector().getSingleton(WarpOperations.class);
        warpOperations.createTable();
        warpOperations.getAllString().forEach(warpBuild -> warpBuilders.add(warpBuilderSerializer.decoder(warpBuild)));
    }

    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
        save();
    }

    @Override
    public int priority() {
        return 2;
    }


    public WarpBuilder getWarp(String name) {
        return warpBuilders.stream()
                .filter(warpBuilders -> warpBuilders.getName().equals(name))
                .findFirst().orElse(null);
    }


    @Override
    public void save() {
        warpOperations.dropTable();
        warpOperations.createTable();
        warpBuilders.forEach(warpBuilder -> {
            warpOperations.insert(warpBuilderSerializer.encode(warpBuilder));
            mainModule.getConnectorSQLite().closeConnection();
        });
    }
}
