package net.giuse.kitmodule.commands;

import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitdelete for delete a kit
 */


public class KitDeleteCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;

    @Inject
    public KitDeleteCommand(MainModule mainModule) {
        super("kitdelete", "lifeserver.kitcreate", false);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        messageBuilder = mainModule.getMessageBuilder();
        setNoPerm("No perms");
        
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player p = (Player) commandSender;
        //Check if the name of kit is present
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-insert-name-kit").sendMessage();
            return;
        }

        //Check if kit exists
        if (kitModule.getKit(args[0].toLowerCase()) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-doesnt-exists").sendMessage();
            return;
        }

        //Delete kit
        kitModule.getCachePlayerKit().asMap().forEach((uuid, playerTimerSystem) -> {
            playerTimerSystem.removeKit(args[0].toLowerCase());
            kitModule.getKitElements().invalidate(args[0].toLowerCase());
            messageBuilder.setCommandSender(p).setIDMessage("kit-removed").sendMessage(new TextReplacer().match("%kit").replaceWith(args[0]));
        });


    }
}
