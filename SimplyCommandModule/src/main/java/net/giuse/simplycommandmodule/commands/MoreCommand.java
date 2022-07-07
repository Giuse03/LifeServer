package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class MoreCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public MoreCommand(MainModule mainModule) {
        super("more", "lifeserver.more", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            return;
        }
        Player player = (Player) commandSender;
        ItemStack newItem = player.getInventory().getItemInMainHand();
        newItem.setAmount(64);
        player.getInventory().setItemInMainHand(newItem);
    }
}
