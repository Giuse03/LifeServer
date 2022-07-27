package net.giuse.simplycommandmodule.commands.time;

import ezmessage.MessageBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class NightCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public NightCommand(MainModule mainModule) {
        super("night", "lifeserver.night");


        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;
        player.getWorld().setTime(18000L);
        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);
        messageBuilder.setCommandSender(commandSender).setIDMessage("time").sendMessage();
    }
}
