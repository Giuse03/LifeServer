package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;

public class EnchantCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public EnchantCommand(MainModule mainModule) {
        super("enchant", "lifeserver.enchant", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage(simplyCommandService.getMex("enchant-usage"));
            } else if (args.length == 1) {
                player.sendMessage(simplyCommandService.getMex("enchant-insert-level"));
            } else {
                if (NumberUtils.isNumber(args[1]) && Integer.parseInt(args[1]) < 0) {
                    player.sendMessage(simplyCommandService.getMex("enchant-correct-insert-level"));
                    return;
                }
                if (Enchantment.getByName(args[0]) != null) {
                    ItemStack hand = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
                    hand.addUnsafeEnchantment(Enchantment.getByName(args[0]), Integer.parseInt(args[1]));
                    itemMeta.addEnchant(Enchantment.getByName(args[0]), Integer.parseInt(args[1]), true);
                    hand.setItemMeta(itemMeta);
                    player.updateInventory();
                } else {
                    player.sendMessage(simplyCommandService.getMex("enchant-invalid").replace("%enchant%", args[0]));
                }
            }
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
