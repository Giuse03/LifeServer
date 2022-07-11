package net.giuse.teleportmodule.subservice;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.WarpOperations;
import net.giuse.teleportmodule.serializer.WarpBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import org.bukkit.Location;

import javax.inject.Inject;

public class WarpLoaderService extends Services implements Savable {

    @Getter
    private Cache<String, Location> warps;
    private final Serializer<WarpSerialized> warpBuilderSerializer = new WarpBuilderSerializer();
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

        //Load Cachec
        warps = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();

        warpOperations = mainModule.getInjector().getSingleton(WarpOperations.class);
        //Load Warps
        warpOperations.getAllString().forEach(warpSerializedString -> {
            WarpSerialized warpSerialized = warpBuilderSerializer.decoder(warpSerializedString);
            warps.put(warpSerialized.getName(), warpSerialized.getLocation());
        });
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
        warps.asMap().forEach((string,location) -> warpOperations.insert(warpBuilderSerializer.encode(new WarpSerialized(string,location))));
    }

    /*
     * Get Warp From Name
     */
    public Location getWarp(String name) {
        return warps.getIfPresent(name.toLowerCase());
    }
}
