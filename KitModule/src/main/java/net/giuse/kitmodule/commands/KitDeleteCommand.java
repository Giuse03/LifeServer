package net.giuse.kitmodule.commands;

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

    private final KitModule kitModule;

    private final MainModule mainModule;

    @Inject
    public KitDeleteCommand(MainModule mainModule) {
        super("kitdelete", "lifeserver.kitdelete", true);
        this.mainModule = mainModule;
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm(kitModule.getMessage("no-perms"));


    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) commandSender;
        if (args.length == 0) {
            p.sendMessage(kitModule.getMessage("kit-insert-name-kit"));
        } else {
            if (kitModule.getKit(args[0]) != null) {

                if (kitModule.getKit(args[0]) != null) {
                    kitModule.getPlayerTimerSystems().forEach(playerTimerSystem -> playerTimerSystem.removeKit(kitModule.getKit(args[0])));
                    kitModule.getKitElements().remove(kitModule.getKit(args[0]));
                } else {
                    p.sendMessage(kitModule.getMessage("kit-doesnt-exists"));
                }
                p.sendMessage(kitModule.getMessage("kit-removed").replace("%kit%", args[0]));
            } else {
                p.sendMessage(kitModule.getMessage("kit-doesnt-exists"));

            }
        }
    }
}
