package net.giuse.teleportmodule.commands.spawn;


import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.SpawnLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SpawnCommand extends AbstractCommand {
    private final SpawnLoaderService spawnLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public SpawnCommand(MainModule mainModule) {
        super("spawn", "lifeserver.spawn", true);
        spawnLoaderService = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        setNoPerm(teleportModule.getMessage("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;

        //Check if there is a spawn
        if (spawnLoaderService.getSpawnBuilder() == null) {
            player.sendMessage(teleportModule.getMessage("no-spawn"));
            return;
        }

        //Teleport to spawn
        teleportModule.getBackLocations().put(player, player.getLocation());
        PaperLib.teleportAsync(player, spawnLoaderService.getSpawnBuilder().getLocation());
        player.sendMessage(teleportModule.getMessage("teleported-spawn"));
    }
}
