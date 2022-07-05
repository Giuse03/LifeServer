package net.giuse.teleportmodule.commands.warp;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpListCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public WarpListCommand(MainModule mainModule) {
        super("warplist", "lifeserver.warp.list", false);
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
        if (warpLoaderService.getWarpBuilders().isEmpty()) {
            p.sendMessage(teleportModule.getMessage("no-warp-available"));
            return;
        }
        StringBuilder sb = new StringBuilder();
        warpLoaderService.getWarpBuilders().forEach(warpBuilder -> {
            sb.append(warpBuilder.getName()).append(",");
        });
        p.sendMessage(teleportModule.getMessage("warp-list").replace("%list%", sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
