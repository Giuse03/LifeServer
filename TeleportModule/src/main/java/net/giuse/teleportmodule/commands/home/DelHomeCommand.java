package net.giuse.teleportmodule.commands.home;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class DelHomeCommand extends AbstractCommand {

    private final HomeLoaderService homeLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public DelHomeCommand(MainModule mainModule) {
        super("delhome", "lifeserver.delhome", true);
        homeLoaderService = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        setNoPerm(teleportModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player sender = (Player) commandSender;

        if (sender.hasPermission("lifeserver.delhome.multiple") || sender.isOp()) {
            if (args.length == 0) {
                if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().size() == 1) {
                    for (String s : homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet()) {
                        homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove(s);
                        sender.sendMessage(teleportModule.getMessage("deleted_home"));
                    }
                } else if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().size() == 0) {
                    sender.sendMessage(teleportModule.getMessage("no_home_found"));
                } else {
                    sender.sendMessage(teleportModule.getMessage("select-home"));
                }
            } else {
                if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().get(args[0]) != null) {
                    homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove(args[0].toLowerCase());
                    sender.sendMessage(teleportModule.getMessage("deleted_home"));
                } else {
                    sender.sendMessage(teleportModule.getMessage("no_home_found"));
                }
            }
        } else {
            if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().get("default") != null) {
                homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove("default");
                sender.sendMessage(teleportModule.getMessage("deleted_home"));
                sender.sendMessage(teleportModule.getMessage("deleted_home"));
            } else {
                sender.sendMessage(teleportModule.getMessage("no_home_found"));
            }
        }
    }
}
