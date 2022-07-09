package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.messages.MessageLoaderKit;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.messages.MessageChat;
import net.giuse.mainmodule.messages.MessageTitle;
import net.giuse.mainmodule.messages.interfaces.Message;
import net.giuse.mainmodule.messages.type.MessageType;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitlist for view a list of kit
 */


public class KitListCommand extends AbstractCommand {
    private final KitModule kitModule;

    private MessageLoaderKit messageLoaderKit;
    @Inject
    public KitListCommand(MainModule mainModule) {
        super("kitlist", "lifeserver.kitlist", true);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm("No Perm");
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
            p.sendMessage("No Perm");
            return;
        }

        //Check if there are kits
        if (kitModule.getKitElements().isEmpty()) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-list-empty");
            return;
        }

        //Show a list of kit to player
        StringBuilder sb = new StringBuilder();
        kitModule.getKitElements().forEach(kitBuilder -> sb.append(kitBuilder.getName()).append(","));
        Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-list","%listkit%="+ sb.deleteCharAt(sb.length() - 1));
    }
}
