package net.giuse.kitmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
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
public class KitCreate extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    private final KitModule kitModule;

    @Inject
    public KitCreate(MainModule mainModule) {
        super("kitcreate", "lifeserver.kitcreate");
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

        Player player = (Player) sender;
        messageBuilder.setCommandSender(player);
        //Check args
        if (args.length == 0) {
            messageBuilder.setIDMessage("kit-insert-name-kit").sendMessage();
            return;
        }
        String kitName = args[0].toLowerCase();

        if (args.length == 1) {
            messageBuilder.setIDMessage("kit-cooldown").sendMessage();

            return;
        }

        //Check if KitExists
        if (kitModule.getKit(kitName) != null) {
            messageBuilder.setIDMessage("kit-already-exists").sendMessage();
            return;
        }

        //Check if Player has inventoryu Empty
        if (isEmpty(player)) {
            messageBuilder.setIDMessage("must-have-item").sendMessage();
            return;
        }

        //Check if Number is valid
        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setIDMessage("kit-cooldown-valid").sendMessage();

            return;
        }
        try {
            if (Integer.parseInt(args[1]) < 0) {
                messageBuilder.setIDMessage("kit-cooldown-valid").sendMessage();
                return;
            }
        } catch (NumberFormatException e) {
            messageBuilder.setIDMessage("kit-cooldown-max").sendMessage();

            return;
        }
        int coolDown = Integer.parseInt(args[1]);

        createKit(player, kitName, coolDown);

    }


    /*
     *  Create Kit
     */
    private void createKit(Player player, String kitName, int coolDownKit) {
        List<ItemStack> inventoryItem = new ArrayList<>();
        Arrays.stream(player.getInventory().getContents()).filter(addItem -> addItem != null && !addItem.getType().equals(Material.AIR)).forEach(inventoryItem::add);
        KitBuilder kitBuilder = new KitBuilder(coolDownKit).setBase(Utils.listItemStackToBase64(inventoryItem));
        kitBuilder.build();
        kitModule.getKitElements().put(kitName, kitBuilder);
        kitModule.getCachePlayerKit().forEach((uuid, playerTimerSystem) -> playerTimerSystem.addKit(kitName, coolDownKit));
        messageBuilder.setIDMessage("kit-created").sendMessage(new TextReplacer().match("%kit%").replaceWith(kitName));
    }

    private boolean isEmpty(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) return false;
        }
        return true;
    }

}
