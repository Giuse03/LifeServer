package net.giuse.simplycommandmodule.commands.time;

import ezmessage.MessageBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TimeCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public TimeCommand(MainModule mainModule) {
        super("time", "lifeserver.time");


        messageBuilder = mainModule.getMessageBuilder();

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("time-usage").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[0])) {
            player.sendMessage("Invalid number");
            return;
        }

        player.getWorld().setTime(Integer.parseInt(args[0]));
        messageBuilder.setCommandSender(commandSender).setIDMessage("time").sendMessage();

    }
}
