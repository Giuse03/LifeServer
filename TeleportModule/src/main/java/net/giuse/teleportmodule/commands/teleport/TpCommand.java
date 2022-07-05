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
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Will be implemented");
            return;
        }
        Player sender = (Player) commandSender;
        if (args.length == 0) {
            sender.sendMessage(teleportModule.getMessage("select-player"));
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(teleportModule.getMessage("player-not-found"));
                return;
            }
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, target.getLocation());
            sender.sendMessage(teleportModule.getMessage("teleport-player").replace("%playername%", sender.getName()));
        } else if (args.length == 2) {
            Player firstTarget = Bukkit.getPlayer(args[0]);
            Player secondTarget = Bukkit.getPlayer(args[1]);

            if (firstTarget == null || secondTarget == null) {
                sender.sendMessage(teleportModule.getMessage("player-not-found"));
                return;
            }
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(firstTarget, secondTarget.getLocation());
            sender.sendMessage(teleportModule.getMessage("teleport-player").replace("%playername%", sender.getName()));
        }
    }
}
