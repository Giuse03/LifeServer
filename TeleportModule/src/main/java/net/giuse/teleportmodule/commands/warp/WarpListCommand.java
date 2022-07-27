package net.giuse.teleportmodule.commands.warp;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpListCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpListCommand(MainModule mainModule) {
        super("warplist", "lifeserver.warp.list");
        messageBuilder = mainModule.getMessageBuilder();
        warpLoaderService = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;

        //Check if there are warp
        if (warpLoaderService.getWarps().size() == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("no-warp-available").sendMessage();
            return;
        }

        //Send player warp Message
        StringBuilder sb = new StringBuilder();
        warpLoaderService.getWarps().forEach((name, location) -> sb.append(name).append(","));
        messageBuilder.setCommandSender(p).setIDMessage("warp-list").sendMessage(new TextReplacer().match("%list%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
