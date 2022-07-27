package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.warpquery.SaveQueryWarp;
import net.giuse.teleportmodule.database.warpquery.WarpQuery;
import net.giuse.teleportmodule.serializer.WarpBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.HashMap;

public class WarpLoaderService extends Services {

    @Getter
    private final Serializer<WarpSerialized> warpBuilderSerializer = new WarpBuilderSerializer();
    @Getter
    private HashMap<String, Location> warps;
    @Inject
    private MainModule mainModule;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Warps...");

        //Load Cache
        warps = new HashMap<>();

        //Load Warps
        mainModule.getInjector().getSingleton(WarpQuery.class).query();

    }


    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Warps...");
        mainModule.getInjector().getSingleton(SaveQueryWarp.class).query();
    }


    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 2;
    }

    /*
     * Get Warp From Name
     */
    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }
}
