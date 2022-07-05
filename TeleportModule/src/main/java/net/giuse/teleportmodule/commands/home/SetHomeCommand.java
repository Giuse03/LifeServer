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
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player sender = (Player) commandSender;
        if (sender.hasPermission("lifeserver.sethome.multiple") || sender.isOp()) {
            for (PermissionAttachmentInfo effectivePermission : sender.getEffectivePermissions()) {
                if (effectivePermission.getPermission().contains("lifeserver.home.multiple.") || sender.isOp()) {
                    int maxHomes;
                    if (!sender.isOp())
                        maxHomes = Integer.parseInt(effectivePermission.getPermission().replace("lifeserver.home.multiple.", ""));
                    else maxHomes = Integer.MAX_VALUE;

                    if (homeLoaderService.getHome(sender.getName()).getLocations().size() != maxHomes) {
                        if (args.length == 0) {
                            sender.sendMessage(teleportModule.getMessage("sethome"));
                            homeLoaderService.getHome(sender.getName()).getLocations().put("default", sender.getLocation());
                        } else {
                            sender.sendMessage(teleportModule.getMessage("sethome"));
                            homeLoaderService.getHome(sender.getName()).getLocations().put(args[0].toLowerCase(), sender.getLocation());
                        }
                    } else {
                        sender.sendMessage(teleportModule.getMessage("max_home_reached"));
                    }
                }
            }
        } else {
            sender.sendMessage(teleportModule.getMessage("sethome"));
            homeLoaderService.getHome(sender.getName()).getLocations().put("default", sender.getLocation());
        }
    }
}
