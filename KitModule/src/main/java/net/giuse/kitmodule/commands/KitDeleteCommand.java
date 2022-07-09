package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kitdelete for delete a kit
 */


public class KitDeleteCommand extends AbstractCommand {
    private final KitModule kitModule;

    @Inject
    public KitDeleteCommand(MainModule mainModule) {
        super("kitdelete", "lifeserver.kitdelete", true);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm("No Perm");

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
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-insert-name-kit");
            return;
        }

        //Check if kit exists
        if (kitModule.getKit(args[0]) == null) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-doesnt-exists");
            return;
        }

        //Delete kit
        kitModule.getPlayerTimerSystems().forEach(playerTimerSystem -> playerTimerSystem.removeKit(kitModule.getKit(args[0])));
        kitModule.getKitElements().remove(kitModule.getKit(args[0]));
        Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-removed","%kit%="+args[0]);

    }
}
