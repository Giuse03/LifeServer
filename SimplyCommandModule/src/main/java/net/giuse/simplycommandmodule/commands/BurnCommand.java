package net.giuse.simplycommandmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class BurnCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public BurnCommand(MainModule mainModule) {
        super("burn", "lifeserver.burn");
        messageBuilder = mainModule.getMessageBuilder();
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {

            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                return;
            }

            Player player = (Player) commandSender;
            player.setFireTicks(20 * 20);
            messageBuilder.setCommandSender(player).setIDMessage("burning").sendMessage(new TextReplacer().match("%seconds%").replaceWith("20"));

            return;
        }

        if (args.length == 1) {

            if (commandSender instanceof ConsoleCommandSender) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                    return;
                }

                target.setFireTicks(20);
                messageBuilder.setCommandSender(commandSender).setIDMessage("burning").sendMessage(
                        new TextReplacer().match("%player_name%").replaceWith(target.getName()),
                        new TextReplacer().match("%seconds%").replaceWith("20"));
                return;
            }

            Player player = (Player) commandSender;

            if (!NumberUtils.isNumber(args[0])) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("burn-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[0]));
                return;
            }
            player.setFireTicks(Integer.parseInt(args[0]));
            messageBuilder.setCommandSender(commandSender).setIDMessage("burning").sendMessage(
                    new TextReplacer().match("%seconds%").replaceWith(args[0]),
                    new TextReplacer().match("%player_name%").replaceWith(player.getName()));
            return;
        }

        if (!commandSender.hasPermission("lifeserver.burn.other")) {
            commandSender.sendMessage("No Perms");
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();

            return;
        }

        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("burn-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[1]));
            return;
        }


        target.setFireTicks(Integer.parseInt(args[1]));
        messageBuilder.setCommandSender(target).setIDMessage("burning").sendMessage(
                new TextReplacer().match("%player_name%").replaceWith(target.getName()),
                new TextReplacer().match("%seconds%").replaceWith(args[1]));

        messageBuilder.setCommandSender(commandSender).setIDMessage("burning").sendMessage(
                new TextReplacer().match("%player_name%").replaceWith(target.getName()),
                new TextReplacer().match("%seconds%").replaceWith(args[1]));
    }
}