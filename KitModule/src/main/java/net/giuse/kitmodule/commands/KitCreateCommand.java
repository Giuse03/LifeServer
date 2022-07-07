package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.UtilsItemStack;
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
        setNoPerm(kitModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(kitModule.getMessage("kit-insert-name-kit"));
        } else if (args.length == 1) {
            p.sendMessage(kitModule.getMessage("kit-cooldown"));
        } else {
            if (!NumberUtils.isNumber(args[1])) {
                p.sendMessage(kitModule.getMessage("kit-cooldown-valid"));
                return;
            }
            //Check if Number is valid
            try {
                if (Integer.parseInt(args[1]) < 0) {
                    p.sendMessage(kitModule.getMessage("kit-cooldown-valid"));
                    return;
                }
            } catch (NumberFormatException e) {
                p.sendMessage(kitModule.getMessage("kit-cooldown-max"));
                return;
            }


            //Check if KitExists
            if (kitModule.getKit(args[0]) != null) {
                p.sendMessage(kitModule.getMessage("kit-already-exists"));
                return;
            }

            //Check if Player has inventoryu Empty
            if (isEmpty(p)) {
                p.sendMessage(kitModule.getMessage("must-have-item"));
                return;
            }

            //Create Kit
            List<ItemStack> itemStackList = new ArrayList<>();
            Arrays.stream(p.getInventory().getContents()).filter(stacks -> stacks != null && !stacks.getType().equals(Material.AIR)).forEach(itemStackList::add);
            KitBuilder kitBuilder = new KitBuilder(args[0], Integer.parseInt(args[1])).setBase(UtilsItemStack.listItemStackToBase64(itemStackList));
            kitModule.getKitElements().add(kitBuilder);
            kitModule.getPlayerTimerSystems().forEach(playerTimerSystem -> playerTimerSystem.addKit(kitBuilder));
            p.sendMessage(kitModule.getMessage("kit-created").replace("%kit%", args[0]));
        }
    }


    private boolean isEmpty(Player p) {
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null) return false;
        }
        return true;
    }

}
