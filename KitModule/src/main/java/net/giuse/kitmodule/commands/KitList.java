package net.giuse.kitmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitlist for view a list of kit
 */


public class KitList extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;

    @Inject
    public KitList(MainModule mainModule) {
        super("kitlist", "lifeserver.kitcreate");
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Check if sender is Console
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        //Check if player has permission
        Player p = (Player) sender;
        if (!p.hasPermission("lifeserver.kit.list")) {
            messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
            return;
        }

        //Check if there are kits
        if (kitModule.getKitElements().size() == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-list-empty").sendMessage();
            return;
        }

        //Show a list of kit to player
        StringBuilder sb = new StringBuilder();
        kitModule.getKitElements().forEach((name, kitBuilder) -> sb.append(StringUtils.capitalize(name)).append(","));
        messageBuilder.setCommandSender(p).setIDMessage("kit-list").sendMessage(new TextReplacer().match("%listkit%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
