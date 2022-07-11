package net.giuse.kitmodule.commands;

import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command /kitcreate for create a kit
 */
public class KitCreateCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    private final KitModule kitModule;

    @Inject
    public KitCreateCommand(MainModule mainModule) {
        super("kitcreate", "lifeserver.kitcreate", false);
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

        Player p = (Player) sender;
        //Check args
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-insert-name-kit").sendMessage();
            return;
        }

        if (args.length == 1) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-cooldown").sendMessage();

            return;
        }

        //Check if Number is valid
        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-cooldown-valid").sendMessage();

            return;
        }
        try {
            if (Integer.parseInt(args[1]) < 0) {
                messageBuilder.setCommandSender(p).setIDMessage("kit-cooldown-valid").sendMessage();
                return;
            }
        } catch (NumberFormatException e) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-cooldown-max").sendMessage();

            return;
        }


        //Check if KitExists
        if (kitModule.getKit(args[0].toLowerCase()) != null) {
            messageBuilder.setCommandSender(p).setIDMessage("kit-already-exists").sendMessage();
            return;
        }

        //Check if Player has inventoryu Empty
        if (isEmpty(p)) {
            messageBuilder.setCommandSender(p).setIDMessage("must-have-item").sendMessage();
            return;
        }

        //Create Kit
        List<ItemStack> itemStackList = new ArrayList<>();
        Arrays.stream(p.getInventory().getContents())
                .filter(stacks -> stacks != null && !stacks.getType().equals(Material.AIR))
                .forEach(itemStackList::add);
        KitBuilder kitBuilder = new KitBuilder(Integer.parseInt(args[1])).setBase(Utils.listItemStackToBase64(itemStackList));
        kitBuilder.build();

        kitModule.getKitElements().put(args[0].toLowerCase(), kitBuilder);

        kitModule.getCachePlayerKit().asMap().forEach((uuid, playerTimerSystem) -> playerTimerSystem.addKit(args[0], kitBuilder));

        messageBuilder.setCommandSender(p).setIDMessage("kit-created").sendMessage(new TextReplacer().match("%kit%").replaceWith(args[0]));

    }


    private boolean isEmpty(Player p) {
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null) return false;
        }
        return true;
    }

}
