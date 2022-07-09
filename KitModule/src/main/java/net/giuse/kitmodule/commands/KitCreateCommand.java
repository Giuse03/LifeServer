package net.giuse.kitmodule.commands;

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
    private final KitModule kitModule;

    @Inject
    public KitCreateCommand(MainModule mainModule) {
        super("kitcreate", "lifeserver.kitcreate", false);
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

        Player p = (Player) sender;
        //Check args
        if (args.length == 0) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-insert-name-kit");
            return;
        }

        if (args.length == 1) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-cooldown");
            return;
        }

        //Check if Number is valid
        if (!NumberUtils.isNumber(args[1])) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-cooldown-valid");
            return;
        }
        try {
            if (Integer.parseInt(args[1]) < 0) {
                Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-cooldown-valid");
                return;
            }
        } catch (NumberFormatException e) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-cooldown-max");

            return;
        }


        //Check if KitExists
        if (kitModule.getKit(args[0]) != null) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-already-exists");
            return;
        }

        //Check if Player has inventoryu Empty
        if (isEmpty(p)) {
            Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"must-have-item");
            return;
        }

        //Create Kit
        List<ItemStack> itemStackList = new ArrayList<>();
        Arrays.stream(p.getInventory().getContents())
                .filter(stacks -> stacks != null && !stacks.getType().equals(Material.AIR))
                .forEach(itemStackList::add);
        KitBuilder kitBuilder = new KitBuilder(args[0], Integer.parseInt(args[1])).setBase(Utils.listItemStackToBase64(itemStackList));
        kitBuilder.build();
        kitModule.getKitElements().add(kitBuilder);
        kitModule.getPlayerTimerSystems().forEach(playerTimerSystem -> playerTimerSystem.addKit(kitBuilder));
        Utils.sendMessage(kitModule.getMessageLoaderKit(),p,"kit-created","%kit%="+args[0]);

    }


    private boolean isEmpty(Player p) {
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null) return false;
        }
        return true;
    }

}
