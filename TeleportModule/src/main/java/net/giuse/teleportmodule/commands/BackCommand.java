package net.giuse.teleportmodule.commands;

import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class BackCommand extends AbstractCommand {
    private final TeleportModule teleportModule;

    @Inject
    public BackCommand(MainModule mainModule) {
        super("back", "lifeserver.back", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        setNoPerm(teleportModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;
        if (teleportModule.getBackLocations().containsKey(player)) {
            player.sendMessage(teleportModule.getMessage("back"));
            Location location = player.getLocation();
            PaperLib.teleportAsync(player, teleportModule.getBackLocations().get(player));
            teleportModule.getBackLocations().put(player, location);
        } else {
            player.sendMessage(teleportModule.getMessage("back-no-location"));

        }
    }
}
