package net.giuse.teleportmodule.subservice;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.database.HomeOperations;
import net.giuse.teleportmodule.serializer.HomeBuilderSerializer;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.UUID;

public class HomeLoaderService extends Services implements Savable {
    @Getter
    private Cache<UUID, Cache<String, Location>> cacheHome;
    private Serializer<HomeSerialized> homeBuilderSerializer;
    @Inject
    private MainModule mainModule;
    private HomeOperations homeOperations;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        homeBuilderSerializer = mainModule.getInjector().getSingleton(HomeBuilderSerializer.class);
        homeOperations = mainModule.getInjector().getSingleton(HomeOperations.class);
        cacheHome = Caffeine.newBuilder().executor(mainModule.getEngine().getForkJoinPool()).build();
        //Load Home
        homeOperations.getAllString().forEach(homeSerializedString -> {
            HomeSerialized homeSerialized = homeBuilderSerializer.decoder(homeSerializedString);
            cacheHome.put(homeSerialized.getOwner(),homeSerialized.getLocations());
        });
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
        save();
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }

    /*
     * Save in a database
     */
    @Override
    public void save() {
        homeOperations.dropTable();
        homeOperations.createTable();
        cacheHome.asMap().forEach(((uuid, hashMap) -> {
            homeOperations.insert(homeBuilderSerializer.encode(new HomeSerialized(uuid,hashMap)));
        }));
    }

    /*
     * Get Home from player's UUID
     */
    public Cache<String, Location> getHome(UUID owner) {
        return cacheHome.getIfPresent(owner);
    }
}
