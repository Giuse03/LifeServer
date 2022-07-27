package net.giuse.simplycommandmodule.commands;


import ezmessage.MessageBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class MoreCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public MoreCommand(MainModule mainModule) {
        super("more", "lifeserver.more");
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;
        ItemStack newItem = player.getInventory().getItemInMainHand();
        newItem.setAmount(64);
        player.getInventory().setItemInMainHand(newItem);
    }
}
