package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kit for view a list of kit and give a kit
 */
public class KitGiveCommand extends AbstractCommand {
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public KitGiveCommand(MainModule mainModule) {
        super("kit", "lifeserver.kit", false);
        this.mainModule = mainModule;
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm("No Perm");

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Check if Sender is Player
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }

        //Check if show kit list or kit gui
        Player p = (Player) sender;
        if (args.length == 0) {

            //Check if player has permission
            if (!p.hasPermission("lifeserver.kit.list")) {
                p.sendMessage("No Perm");

                return;
            }

            //check if show gui or list
            if (mainModule.getConfig().getBoolean("use-kit-gui")) {
                KitGui guiManager = mainModule.getInjector().getSingleton(KitGui.class);
                guiManager.openInv(p);
                return;
            }

            //Check if there are kits
            if (kitModule.getKitElements().isEmpty()) {
                Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-list-empty");
                return;
            }
            StringBuilder sb = new StringBuilder();
            kitModule.getKitElements().forEach(kitBuilder -> sb.append(kitBuilder.getName()).append(","));
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-list","%listkit%="+ sb.deleteCharAt(sb.length() - 1));

            return;
        }

        //Check if player has permission
        if (!p.hasPermission("lifeserver.kit." + args[0])) {
            p.sendMessage("No Perm");

            return;
        }

        //Check if kit exists
        if (kitModule.getKit(args[0]) == null) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-doesnt-exists");
            return;
        }

        //Check if player can get kit
        KitBuilder kitBuilder = kitModule.getKit(args[0]);
        KitCooldown kitCooldown = kitModule.getPlayerTime(p.getUniqueId()).getKitsCooldown().stream().filter(kitCooldowns -> kitCooldowns.getKitBuilder().equals(kitBuilder)).findFirst().get();
        if (kitCooldown.getVariableCoolDown() != 0) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-wait","%time%=" + Utils.formatTime(kitCooldown.getVariableCoolDown()));
            return;
        }

        //Give kit to Player
        kitModule.getPlayerTime(p.getUniqueId()).start(kitCooldown);
        kitBuilder.giveItems(p);
        Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-receive","%kit%=" + kitBuilder.getName());
    }
}

