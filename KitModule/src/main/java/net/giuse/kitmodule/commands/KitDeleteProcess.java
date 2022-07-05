package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class KitDeleteProcess extends AbstractCommand {

    private final KitModule kitModule;

    @Inject
    public KitDeleteProcess(MainModule mainModule) {
        super("kitdelete", "lifeserver.kitdelete", true);
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
                    kitModule.getKitElements().remove(kitModule.getKitBuilderSerializer().encode(kitModule.getKit(args[0])));
                } else {
                    p.sendMessage(kitModule.getMessage("kit-doesnt-exists"));
                }

                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    PlayerTimerSystem playerTimerSystem = kitModule.getPlayerTime(onlinePlayers.getUniqueId(), kitModule.getKit(args[0]));
                    Bukkit.getScheduler().cancelTask(playerTimerSystem.getTaskId());
                    kitModule.getPlayerTimerSystems().remove(playerTimerSystem);
                }

                p.sendMessage(kitModule.getMessage("kit-removed").replace("%kit%", args[0]));
            } else {
                p.sendMessage(kitModule.getMessage("kit-doesnt-exists"));

            }
        }
    }
}
