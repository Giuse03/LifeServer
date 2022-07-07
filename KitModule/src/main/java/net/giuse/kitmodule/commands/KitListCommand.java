package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitlist for view a list of kit
 */


public class KitListCommand extends AbstractCommand {
    private final KitModule kitModule;

    @Inject
    public KitListCommand(MainModule mainModule) {
        super("kitlist", "lifeserver.kitlist", true);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm(kitModule.getMessage("no-perms"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Check if sender is a Player
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        //Check if player has permission
        Player p = (Player) sender;
        if (p.hasPermission("lifeserver.kit.list")) {
            p.sendMessage(kitModule.getMessage("no-perms"));
            return;
        }

        //Check if there are kits
        if (kitModule.getKitElements().isEmpty()) {
            p.sendMessage(kitModule.getMessage("kit-list-empty"));
            return;
        }

        //Show a list of kit to player
        StringBuilder sb = new StringBuilder();
        kitModule.getKitElements().forEach(kitBuilder -> sb.append(kitBuilder.getName()).append(","));
        p.sendMessage(kitModule.getMessage("kit-list").replace("%listkit%", sb.deleteCharAt(sb.length() - 1).toString()));
    }
}
