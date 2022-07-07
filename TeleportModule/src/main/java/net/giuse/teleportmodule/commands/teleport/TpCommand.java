package net.giuse.teleportmodule.commands.teleport;

import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpCommand extends AbstractCommand {
    private final TeleportModule teleportModule;

    @Inject
    public TpCommand(MainModule mainModule) {
        super("tp", "lifeserver.tp", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        setNoPerm(teleportModule.getMessage("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Will be implemented");
            return;
        }
        Player sender = (Player) commandSender;

        //Check if player isn't selected
        if (args.length == 0) {
            sender.sendMessage(teleportModule.getMessage("select-player"));
            return;
        }

        //Teleport to the target
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            //Check if target is online
            if (target == null) {
                sender.sendMessage(teleportModule.getMessage("player-not-found"));
                return;
            }

            //Teleport Target
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, target.getLocation());
            sender.sendMessage(teleportModule.getMessage("teleport-player").replace("%playername%", sender.getName()));
            return;
        }

        //Teleport First Target to Second Target
        Player firstTarget = Bukkit.getPlayer(args[0]);
        Player secondTarget = Bukkit.getPlayer(args[1]);

        //Check if targets are online
        if (firstTarget == null || secondTarget == null) {
            sender.sendMessage(teleportModule.getMessage("player-not-found"));
            return;
        }

        //Teleport Targets
        teleportModule.getBackLocations().put(sender, sender.getLocation());
        PaperLib.teleportAsync(firstTarget, secondTarget.getLocation());
        sender.sendMessage(teleportModule.getMessage("teleport-player").replace("%playername%", sender.getName()));
    }
}
