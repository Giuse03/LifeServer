package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

/**
 * Command /kitcreate for create a kit
 */
public class KitCreateCommand extends AbstractCommand {

    private final KitModule kitModule;
    private final MainModule mainModule;

    @Inject
    public KitCreateCommand(MainModule mainModule) {
        super("kitcreate", "lifeserver.kitcreate", true);
        this.mainModule = mainModule;
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
            if (kitModule.getKit(args[0]) == null) {
                if (!isEmpty(p)) {
                    KitBuilder kitBuilder = new KitBuilder();
                    kitBuilder.setCoolDown(Integer.parseInt(args[1]));
                    kitBuilder.setName(args[0]);
                    for (ItemStack stack : p.getInventory().getContents()) {
                        if (stack != null && !stack.getType().equals(Material.AIR)) {
                            kitBuilder.getItems().add(kitModule.getItemStackSerializer().encode(stack));
                        }
                    }
                    kitModule.getKitElements().add(kitBuilder);
                    for (PlayerTimerSystem playerTimerSystem : kitModule.getPlayerTimerSystems()) {
                        playerTimerSystem.addKit(kitBuilder);
                    }
                    p.sendMessage(kitModule.getMessage("kit-created").replace("%kit%", args[0]));
                } else {
                    p.sendMessage(kitModule.getMessage("must-have-item"));
                }
            } else {
                p.sendMessage(kitModule.getMessage("kit-already-exists"));
            }
        }
    }

    private boolean isEmpty(Player p) {
        for (ItemStack it : p.getInventory().getContents()) {
            if (it != null) return false;
        }
        return true;
    }

}
