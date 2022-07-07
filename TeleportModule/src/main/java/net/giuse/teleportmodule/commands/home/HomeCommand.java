package net.giuse.teleportmodule.commands.home;

import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HomeCommand extends AbstractCommand {

    private final HomeLoaderService homeLoaderService;
    private final TeleportModule teleportModule;


    @Inject
    public HomeCommand(MainModule mainModule) {
        super("home", "lifeserver.home", true);
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
        if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().isEmpty()) {
            sender.sendMessage(teleportModule.getMessage("no_home_found"));
        }

        //Check if player has multiple home
        if (sender.hasPermission("lifeserver.home.multiple") || sender.isOp()) {

            //Looking for a home list
            if (args.length == 0) {

                //Send Home List to Player
                if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet().size() > 1) {
                    StringBuilder listHome = new StringBuilder();

                    int i = 0;
                    for (String s : homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet()) {
                        i++;
                        if (i == homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet().size()) {
                            listHome.append(s);
                        }
                        listHome.append(s).append(",");
                    }

                    sender.sendMessage(teleportModule.getMessage("home_list").replace("%list%", listHome.toString()));
                    return;
                }

                //Check if player has one home, and teleport him
                for (String s : homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet()) {
                    teleportModule.getBackLocations().put(sender, sender.getLocation());
                    PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getUniqueId()).getLocations().get(s));
                    sender.sendMessage(teleportModule.getMessage("teleported"));
                }
                return;
            }

            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getUniqueId()).getLocations().get(args[0].toLowerCase()));
            sender.sendMessage(teleportModule.getMessage("teleported"));
            return;
        }

        //Teleport player to the default home
        for (String s : homeLoaderService.getHome(sender.getUniqueId()).getLocations().keySet()) {
            teleportModule.getBackLocations().put(sender, sender.getLocation());
            PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getUniqueId()).getLocations().get(s));
            sender.sendMessage(teleportModule.getMessage("teleported"));
            break;
        }
    }
}
