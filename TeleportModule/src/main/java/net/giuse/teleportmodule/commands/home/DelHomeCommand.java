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
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player sender = (Player) commandSender;

        //Check if player has home
        if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().size() == 0) {
            sender.sendMessage(teleportModule.getMessage("no_home_found"));
        }

        //Check if player has multiple home
        if (sender.hasPermission("lifeserver.delhome.multiple") || sender.isOp()) {
            if (args.length == 0) {

                //Check if player has one home
                if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().size() == 1) {
                    homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet().forEach(home -> homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove(home));
                    sender.sendMessage(teleportModule.getMessage("deleted_home"));
                    return;
                }

                sender.sendMessage(teleportModule.getMessage("select-home"));
            }

            //Check if home exists
            if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().get(args[0]) == null) {
                sender.sendMessage(teleportModule.getMessage("no_home_found"));
                return;
            }

            //Delete home
            homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove(args[0].toLowerCase());
            sender.sendMessage(teleportModule.getMessage("deleted_home"));
        }

        //Delete Home
        homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet().forEach(home -> homeLoaderService.getHome(sender.getUniqueId()).getLocations().remove(home));
        sender.sendMessage(teleportModule.getMessage("deleted_home"));
    }
}
