package net.giuse.kitmodule.commands;

import net.giuse.engine.Worker;
import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
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
public class KitGive extends AbstractCommand {
    private final MainModule mainModule;
    private final MessageBuilder messageBuilder;
    private final KitModule kitModule;
    private final Worker worker;

    @Inject
    public KitGive(MainModule mainModule, Worker worker) {
        super("kit", "lifeserver.kitcreate", true);
        this.worker = worker;
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
        Player player = (Player) sender;
        messageBuilder.setCommandSender(player);
        if (args.length == 0) {

            //Check if player has permission
            if (!player.hasPermission("lifeserver.kit.list")) {
                messageBuilder.setIDMessage("no-perms").sendMessage();
                return;
            }

            //check if show gui or list
            if (mainModule.getConfig().getBoolean("use-kit-gui")) {
                KitGui kitInventory = mainModule.getInjector().getSingleton(KitGui.class);
                worker.executeProcess(() -> kitInventory.openInv(player), false);
                return;
            }

            //Check if there are kits
            if (kitModule.getKitElements().size() == 0) {
                messageBuilder.setIDMessage("kit-list-empty").sendMessage();
                return;
            }
            StringBuilder sb = new StringBuilder();
            kitModule.getKitElements().forEach((name, kitBuilder) -> sb.append(StringUtils.capitalize(name)).append(","));
            messageBuilder.setIDMessage("kit-list").sendMessage(new TextReplacer().match("%listkit%").replaceWith(sb.deleteCharAt(sb.length() - 1).toString()));
            return;
        }
        String kitName = args[0];
        //Check if player has permission
        if (!player.hasPermission("lifeserver.kit." + kitName)) {
            messageBuilder.setIDMessage("no-perms").sendMessage();
            return;
        }

        //Check if kit exists
        if (kitModule.getKit(kitName.toLowerCase()) == null) {
            messageBuilder.setIDMessage("kit-doesnt-exists").sendMessage();
            return;
        }


        //Check if player can get kit
        KitBuilder kitBuilder = kitModule.getKit(kitName);
        int actualCooldown = kitModule.getPlayerCooldown(player.getUniqueId()).getActualCooldown(kitName);

        if (actualCooldown != 0) {
            messageBuilder.setIDMessage("kit-wait").sendMessage(new TextReplacer().match("%time%").replaceWith(Utils.formatTime(actualCooldown)));
            return;
        }

        //Give kit to Player
        kitModule.getPlayerCooldown(player.getUniqueId()).addKit(kitName.toLowerCase(), kitBuilder.getCoolDown());
        kitBuilder.giveItems(player);
        messageBuilder.setIDMessage("kit-receive").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));

    }
}

