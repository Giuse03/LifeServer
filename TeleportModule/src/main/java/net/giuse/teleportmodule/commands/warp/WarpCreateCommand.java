package net.giuse.teleportmodule.commands.warp;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.WarpBuilder;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpCreateCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public WarpCreateCommand(MainModule mainModule) {
        super("warpcreate", "lifeserver.warpcreate", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        warpLoaderService = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
        setNoPerm(teleportModule.getMessage("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;

        //Check if name length is 0
        if (args.length == 0) {
            p.sendMessage(teleportModule.getMessage("warp-insert-name"));
            return;
        }

        //Check warp exists
        if (warpLoaderService.getWarp(args[0]) != null) {
            p.sendMessage(teleportModule.getMessage("warp-already-exists"));
            return;
        }

        //Check if name contains illegal characters
        if (args[0].contains(":") || args[0].contains(",")) {
            p.sendMessage("§cCharacter §4 ':' or ',' §c isn't allowed in warp name!");
            return;
        }
        //Create warp
        warpLoaderService.getWarpBuilders().add(new WarpBuilder(args[0], p.getLocation()));
        p.sendMessage(teleportModule.getMessage("warp-created").replace("%name%", args[0]));
    }
}
