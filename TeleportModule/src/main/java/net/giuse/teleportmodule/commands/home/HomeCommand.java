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
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player sender = (Player) commandSender;

        if (sender.hasPermission("lifeserver.home.multiple") || sender.isOp()) {
            if (args.length == 0) {
                if (homeLoaderService.getHome(sender.getName()).getLocations().keySet().size() > 1) {
                    StringBuilder listHome = new StringBuilder();
                    int i = 0;
                    for (String s : homeLoaderService.getHome(sender.getName()).getLocations().keySet()) {
                        i++;
                        if (i == homeLoaderService.getHome(sender.getName()).getLocations().keySet().size()) {
                            listHome.append(s);
                        } else {
                            listHome.append(s).append(",");
                        }
                    }
                    sender.sendMessage(teleportModule.getMessage("home_list").replace("%list%", listHome.toString()));
                } else if (!homeLoaderService.getHome(sender.getName()).getLocations().isEmpty()) {

                    for (String s : homeLoaderService.getHome(sender.getName()).getLocations().keySet()) {
                        teleportModule.getBackLocations().put(sender, sender.getLocation());
                        PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getName()).getLocations().get(s));
                        sender.sendMessage(teleportModule.getMessage("teleported"));
                    }
                } else {
                    sender.sendMessage(teleportModule.getMessage("no_home_found"));
                }

            } else {
                if (homeLoaderService.getHome(sender.getName()).getLocations().get(args[0].toLowerCase()) != null) {
                    teleportModule.getBackLocations().put(sender, sender.getLocation());
                    PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getName()).getLocations().get(args[0].toLowerCase()));

                    sender.sendMessage(teleportModule.getMessage("teleported"));
                } else {
                    sender.sendMessage(teleportModule.getMessage("no_home_found"));
                }
            }
        } else {
            if (homeLoaderService.getHome(sender.getName()).getLocations().get("default") != null) {
                teleportModule.getBackLocations().put(sender, sender.getLocation());
                PaperLib.teleportAsync(sender, homeLoaderService.getHome(sender.getName()).getLocations().get("default"));
                sender.sendMessage(teleportModule.getMessage("teleported"));
            } else {
                sender.sendMessage(teleportModule.getMessage("no_home_found"));
            }
        }
    }
}
