package net.giuse.simplycommandmodule;


import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.simplycommandmodule.events.FoodEvent;
import net.giuse.simplycommandmodule.files.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class SimplyCommandService extends Services {

    @Getter
    private final HashMap<String, String> message = new HashMap<>();

    @Getter
    private final ArrayList<String> stringsNameGods = new ArrayList<>();

    @Inject
    private MainModule mainModule;


    /**
     * This is Module of Simply Command
     * Most of these commands are not async executable so they will not pass through the process engine
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eGeneral Commands§9] §7Loading General Commands...");
        FileManager fileManager;
        ReflectionsFiles.loadFiles(fileManager = new FileManager());
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_simple_command.yml")) == 0) {
            fileManager.messagesMsgLoader();
        }
        for (String messageConfig : fileManager.getMessageSimpleFileYml().getConfigurationSection("messages.simplecommand").getKeys(true)) {
            message.put(messageConfig, fileManager.getMessageSimpleFileYml().getString("messages.simplecommand." + messageConfig));
        }
        message.put("no-perms", mainModule.getConfig().getString("no-perms"));


        if (mainModule.getConfig().getBoolean("no-hunger")) {
            mainModule.getServer().getPluginManager().registerEvents(new FoodEvent(), mainModule);
        }
        if (mainModule.getConfig().getBoolean("always-day")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (World world : Bukkit.getWorlds()) {
                        world.setTime(24000L);
                        world.setStorm(false);
                        world.setThundering(false);
                    }
                }
            }.runTaskTimer(mainModule, 10 * 20, 10 * 20);
        }

    }

    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eGeneral Commands§9] §7Unloading General Commands...");
    }

    @Override
    public int priority() {
        return 0;
    }


    public String getMex(String key) {
        return ChatColor.translateAlternateColorCodes('&', message.get(key));
    }
}
