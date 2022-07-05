package net.giuse.teleportmodule;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.events.EntityBackOnDeath;
import net.giuse.teleportmodule.files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

public class TeleportModule extends Services {
    @Getter
    private final HashMap<String, String> message = new HashMap<>();
    @Getter
    private final HashMap<Player, Location> backLocations = new HashMap<>();
    @Inject
    private MainModule mainModule;
    @Getter
    private FileManager fileManager;

    @Override
    @SneakyThrows
    public void load() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Loading");
        ReflectionsFiles.loadFiles(fileManager = new FileManager());
        if (mainModule.getConfig().getBoolean("allow-back-on-death"))
            Bukkit.getServer().getPluginManager().registerEvents(mainModule.getInjector().getSingleton(EntityBackOnDeath.class), mainModule);
        message.put("no-perms", mainModule.getConfig().getString("no-perms"));

    }

    @Override
    public void unload() {
        Bukkit.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9 Unloaded");
    }

    @Override
    public int priority() {
        return 0;
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', message.get(key));
    }


}
