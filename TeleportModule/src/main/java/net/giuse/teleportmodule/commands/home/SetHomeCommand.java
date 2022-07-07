package net.giuse.teleportmodule.commands.home;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import javax.inject.Inject;

public class SetHomeCommand extends AbstractCommand {
    private final HomeLoaderService homeLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public SetHomeCommand(MainModule mainModule) {
        super("sethome", "lifeserver.sethome", true);
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

        //Check if player has multiple home
        if (sender.hasPermission("lifeserver.sethome.multiple") || sender.isOp()) {
            for (PermissionAttachmentInfo effectivePermission : sender.getEffectivePermissions()) {
                if (effectivePermission.getPermission().contains("lifeserver.home.multiple.") || sender.isOp()) {

                    //Get Max Home Available
                    int maxHomes;
                    if (!sender.isOp()) {
                        maxHomes = Integer.parseInt(effectivePermission.getPermission().replace("lifeserver.home.multiple.", ""));
                    } else {
                        maxHomes = Integer.MAX_VALUE;
                    }

                    //Check if player has reached max home
                    if (homeLoaderService.getHome(sender.getUniqueId()).getLocations().size() == maxHomes) {
                        sender.sendMessage(teleportModule.getMessage("max_home_reached"));
                        return;
                    }

                    //Check if there is a name
                    if (args.length == 0) {
                        sender.sendMessage(teleportModule.getMessage("sethome"));
                        //Set Home
                        homeLoaderService.getHome(sender.getUniqueId()).getLocations().put("default", sender.getLocation());
                        return;
                    }

                    //Check if home name has illegal character
                    if (args[0].contains(":") || args[0].contains(",") || args[0].contains(";")) {
                        sender.sendMessage("§cCharacter §4 ':' or ',' or ';' §c isn't allowed in home name!");
                        return;
                    }

                    //Set Home
                    sender.sendMessage(teleportModule.getMessage("sethome"));
                    homeLoaderService.getHome(sender.getUniqueId()).getLocations().put(args[0].toLowerCase(), sender.getLocation());
                }
            }
            return;
        }

        //Set Home
        sender.sendMessage(teleportModule.getMessage("sethome"));
        homeLoaderService.getHome(sender.getUniqueId()).getLocations().put("default", sender.getLocation());
    }
}
