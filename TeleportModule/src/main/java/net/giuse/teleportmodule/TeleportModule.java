package net.giuse.teleportmodule;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.events.EntityBackOnDeath;
import net.giuse.teleportmodule.files.FileManager;
import net.giuse.teleportmodule.messageloader.MessageLoaderTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

public class TeleportModule extends Services {

    @Getter
    private final HashMap<Player, Location> backLocations = new HashMap<>();
    @Inject
    private MainModule mainModule;
    @Getter
    private FileManager fileManager;


    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Loading");
        //Load Files
        ReflectionsFiles.loadFiles(fileManager = new FileManager());

        //Load Message
        mainModule.getInjector().getSingleton(MessageLoaderTeleport.class).load();
        //Check if load back-on-death is active
        if (mainModule.getConfig().getBoolean("allow-back-on-death")) {
            Bukkit.getServer().getPluginManager().registerEvents(mainModule.getInjector().getSingleton(EntityBackOnDeath.class), mainModule);
        }
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Unloaded");
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 0;
    }

}
