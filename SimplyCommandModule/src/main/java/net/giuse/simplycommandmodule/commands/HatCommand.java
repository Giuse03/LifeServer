package net.giuse.simplycommandmodule.commands;


import ezmessage.MessageBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HatCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public HatCommand(MainModule mainModule) {
        super("hat", "lifeserver.hat");
        messageBuilder = mainModule.getMessageBuilder();


    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }

        Player player = (Player) commandSender;
        if (player.getInventory().getItemInMainHand() != null) {
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(null);
        }
    }
}
