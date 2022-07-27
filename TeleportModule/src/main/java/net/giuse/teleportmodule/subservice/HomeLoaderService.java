package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.homequery.HomeQuery;
import net.giuse.teleportmodule.database.homequery.SaveQueryHome;
import net.giuse.teleportmodule.serializer.HomeBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;

public class HomeLoaderService extends Services {
    @Getter
    private HashMap<UUID, HashMap<String, Location>> cacheHome;

    @Getter
    private Serializer<HomeSerialized> homeBuilderSerializer;
    @Inject
    private MainModule mainModule;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        homeBuilderSerializer = mainModule.getInjector().getSingleton(HomeBuilderSerializer.class);
        cacheHome = new HashMap<>();

        //Load Home
        mainModule.getInjector().getSingleton(HomeQuery.class).query();
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
        mainModule.getInjector().getSingleton(SaveQueryHome.class).query();

    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }

    /*
     * Get Home from player's UUID
     */
    public HashMap<String, Location> getHome(UUID owner) {
        return cacheHome.get(owner);
    }
}
