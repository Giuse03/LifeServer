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

    @Getter
    private final Set<WarpBuilder> warpBuilders = new HashSet<>();
    private final Serializer<WarpBuilder> warpBuilderSerializer = new WarpBuilderSerializer();
    @Inject
    private MainModule mainModule;
    private WarpOperations warpOperations;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");

        warpOperations = mainModule.getInjector().getSingleton(WarpOperations.class);
        //Load Warps
        warpOperations.getAllString().forEach(warpBuild -> warpBuilders.add(warpBuilderSerializer.decoder(warpBuild)));
    }


    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
        save();
    }


    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 2;
    }

    /*
     *  Save warp in a database
     */
    @Override
    public void save() {
        warpOperations.dropTable();
        warpOperations.createTable();
        warpBuilders.forEach(warpBuilder -> warpOperations.insert(warpBuilderSerializer.encode(warpBuilder)));
        mainModule.getConnectorSQLite().closeConnection();
    }

    /*
     * Get Warp From Name
     */
    public WarpBuilder getWarp(String name) {
        return warpBuilders.stream()
                .filter(warpBuilders -> warpBuilders.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
