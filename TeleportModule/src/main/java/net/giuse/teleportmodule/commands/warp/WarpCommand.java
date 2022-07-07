package net.giuse.teleportmodule.commands.warp;

import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.gui.WarpGui;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;
    private final TeleportModule teleportModule;
    private final MainModule mainModule;

    @Inject
    public WarpCommand(MainModule mainModule) {
        super("warp", "lifeserver.warp.list", false);
        this.mainModule = mainModule;
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

        //Show warp gui or warp list
        if (args.length == 0) {

            //Check if gui is active
            if (mainModule.getConfig().getBoolean("use-warp-gui")) {
                WarpGui warpGui = mainModule.getInjector().getSingleton(WarpGui.class);
                warpGui.openInv(p);
                return;
            }

            //Check if warp list is empty
            if (warpLoaderService.getWarpBuilders().isEmpty()) {
                p.sendMessage(teleportModule.getMessage("no-warp-available"));
                return;
            }

            //Send Warp List
            StringBuilder sb = new StringBuilder();
            warpLoaderService.getWarpBuilders().forEach(warpBuilder -> sb.append(warpBuilder.getName()).append(","));
            p.sendMessage(teleportModule.getMessage("warp-list").replace("%list%", sb.deleteCharAt(sb.length() - 1).toString()));
            return;
        }

        //Check if Player has permission for a warp
        if (!p.hasPermission("lifeserver.warp." + args[0])) {
            commandSender.sendMessage(teleportModule.getMessage("no-perms"));
            return;
        }
        //Check if warp exists
        if (warpLoaderService.getWarp(args[0]) == null) {
            p.sendMessage(teleportModule.getMessage("warp-no-exists").replace("%name%", args[0]));
            return;
        }

        //Teleport to a Warp
        teleportModule.getBackLocations().put(p, p.getLocation());
        PaperLib.teleportAsync(p, warpLoaderService.getWarp(args[0]).getLocation());
        p.sendMessage(teleportModule.getMessage("warp-teleport").replace("%name%", args[0]));


    }
}
