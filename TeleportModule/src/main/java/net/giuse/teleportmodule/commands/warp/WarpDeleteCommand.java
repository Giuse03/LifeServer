package net.giuse.teleportmodule.commands.warp;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpDeleteCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;

    private final TeleportModule teleportModule;

    @Inject
    public WarpDeleteCommand(MainModule mainModule) {
        super("warpdelete", "lifeserver.warpdelete", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        warpLoaderService = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
        setNoPerm(teleportModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(teleportModule.getMessage("warp-insert-name"));
        } else {
            if (warpLoaderService.getWarp(args[0]) != null) {
                warpLoaderService.getWarpBuilders().remove(warpLoaderService.getWarp(args[0]));
                p.sendMessage(teleportModule.getMessage("warp-removed").replace("%name%", args[0]));
            } else {
                p.sendMessage(teleportModule.getMessage("warp-no-exists"));
            }
        }
    }
}
