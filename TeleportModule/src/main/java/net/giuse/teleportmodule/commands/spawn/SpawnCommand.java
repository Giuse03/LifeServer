package net.giuse.teleportmodule.commands.spawn;


import ezmessage.MessageBuilder;
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
    private final MessageBuilder messageBuilder;

    private final TeleportModule teleportModule;

    @Inject
    public SpawnCommand(MainModule mainModule) {
        super("spawn", "lifeserver.spawn");
        spawnLoaderService = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        messageBuilder = mainModule.getMessageBuilder();


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
            messageBuilder.setCommandSender(commandSender).setIDMessage("no-spawn").sendMessage();
            return;
        }

        //Teleport to spawn
        teleportModule.getBackLocations().put(player, player.getLocation());
        PaperLib.teleportAsync(player, spawnLoaderService.getSpawnBuilder().getLocation());
        messageBuilder.setCommandSender(commandSender).setIDMessage("teleported-spawn").sendMessage();

    }
}
