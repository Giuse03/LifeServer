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

public class WarpDeleteCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;

    private final MessageBuilder messageBuilder;

    @Inject
    public WarpDeleteCommand(MainModule mainModule) {
        super("warpdelete", "lifeserver.warpdelete");
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

        //Check if name length is 0
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-insert-name").sendMessage();
            return;
        }

        //Check if warp exists
        if (warpLoaderService.getWarp(args[0]) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-no-exists").sendMessage();
            return;
        }

        //Delete Warp
        warpLoaderService.getWarps().remove(args[0]);
        messageBuilder.setCommandSender(p).setIDMessage("warp-removed").sendMessage(new TextReplacer().match("%name%").replaceWith(args[0]));
    }

}
