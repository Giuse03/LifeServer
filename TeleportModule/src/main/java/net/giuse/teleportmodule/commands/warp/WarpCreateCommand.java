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

public class WarpCreateCommand extends AbstractCommand {
    private final WarpLoaderService warpLoaderService;
    private final MessageBuilder messageBuilder;

    @Inject
    public WarpCreateCommand(MainModule mainModule) {
        super("warpcreate", "lifeserver.warpcreate");
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

        //Check warp exists
        if (warpLoaderService.getWarp(args[0]) != null) {
            messageBuilder.setCommandSender(p).setIDMessage("warp-already-exists").sendMessage();
            return;
        }

        //Check if name contains illegal characters
        if (args[0].contains(":") || args[0].contains(",")) {
            p.sendMessage("§cCharacter §4 ':' or ',' §c isn't allowed in warp name!");
            return;
        }
        //Create warp
        warpLoaderService.getWarps().put(args[0].toLowerCase(), p.getLocation());
        messageBuilder.setCommandSender(p).setIDMessage("warp-created").sendMessage(new TextReplacer().match("%name%").replaceWith(args[0]));
    }
}
