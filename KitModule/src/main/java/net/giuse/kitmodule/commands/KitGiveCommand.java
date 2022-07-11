package net.giuse.kitmodule.commands;

import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kit for view a list of kit and give a kit
 */
public class KitGiveCommand extends AbstractCommand {
    private final MainModule mainModule;
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;

    @Inject
    public KitGiveCommand(MainModule mainModule) {
        super("kit", "lifeserver.kitcreate", false);
        this.mainModule = mainModule;
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        messageBuilder = mainModule.getMessageBuilder();
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
                messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
                return;
            }

            //check if show gui or list
            if (mainModule.getConfig().getBoolean("use-kit-gui")) {
                KitGui guiManager = mainModule.getInjector().getSingleton(KitGui.class);
                guiManager.openInv(p);
                return;
            }

            //Check if there are kits
            if (kitModule.getKitElements().estimatedSize() == 0) {
                messageBuilder.setCommandSender(p).setIDMessage("kit-list-empty").sendMessage();
                return;
            }
            StringBuilder sb = new StringBuilder();
            kitModule.getKitElements().asMap().forEach((name, kitBuilder) -> sb.append(StringUtils.capitalize(name)).append(","));
            messageBuilder.setCommandSender(p).setIDMessage("kit-list").sendMessage(new TextReplacer().match("%listkit%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
            return;
        }

        //Check if player has permission
        if (!p.hasPermission("lifeserver.kit." + args[0])) {
            messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();

            return;
        }

        //Check if kit exists
        if (kitModule.getKit(args[0].toLowerCase()) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-doesnt-exists").sendMessage();
            return;
        }

        //Check if player can get kit
        KitBuilder kitBuilder = kitModule.getKit(args[0]);
        KitCooldown kitCooldown = kitModule.getPlayerTime(p.getUniqueId()).getKitsCooldown().stream().filter(kitCooldowns -> kitCooldowns.getKitBuilder().equals(kitBuilder)).findFirst().get();
        if (kitCooldown.getVariableCoolDown() != 0) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-wait").sendMessage(new TextReplacer().match("%time%").replaceWith(Utils.formatTime(kitCooldown.getVariableCoolDown())));
            return;
        }

        //Give kit to Player
        kitModule.getPlayerTime(p.getUniqueId()).start(kitCooldown);
        kitBuilder.giveItems(p);
        messageBuilder.setCommandSender(p).setIDMessage("kit-receive").sendMessage(new TextReplacer().match("%kit%").replaceWith(StringUtils.capitalize(args[0])));

    }
}

