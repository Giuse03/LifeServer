package net.giuse.kitmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
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


public class KitDelete extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;

    @Inject
    public KitDelete(MainModule mainModule) {
        super("kitdelete", "lifeserver.kitcreate");
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        Player player = (Player) commandSender;
        messageBuilder.setCommandSender(player);
        //Check if the name of kit is present
        if (!hasEnoughArgs(args)) return;
        String kitName = args[0].toLowerCase();

        //Check if kit exists
        if (!checkIfKitExists(kitName)) return;

        //Delete kit
        deleteKit(kitName);
    }

    private void deleteKit(String kitName) {
        kitModule.getCachePlayerKit().forEach((uuid, playerTimerSystem) -> {
            playerTimerSystem.removeKit(kitName);
            kitModule.getKitElements().remove(kitName);
            messageBuilder.setIDMessage("kit-removed").sendMessage(new TextReplacer().match("%kit").replaceWith(kitName));
        });
    }

    private boolean checkIfKitExists(String kitname) {
        if (kitModule.getKit(kitname) == null) {
            messageBuilder.setIDMessage("kit-doesnt-exists").sendMessage();
            return false;
        }
        return true;
    }

    private boolean hasEnoughArgs(String[] args) {
        if (args.length == 0) {
            messageBuilder.setIDMessage("kit-insert-name-kit").sendMessage();
            return false;
        }
        return true;
    }
}
