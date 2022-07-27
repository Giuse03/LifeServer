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

public class SpeedCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public SpeedCommand(MainModule mainModule) {
        super("speed", "lifeserver.speed");
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
            messageBuilder.setCommandSender(commandSender).setIDMessage("speed-usage").sendMessage();

            return;
        }
        if (args.length == 1) {
            if (!NumberUtils.isNumber(args[0])) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("speed-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[0]));
                return;
            }

            if (Integer.parseInt(args[0]) >= 0 && (Integer.parseInt(args[0]) <= 10)) {
                setSpeed(player, Float.valueOf(args[0]));
                return;
            }

            messageBuilder.setCommandSender(commandSender).setIDMessage("speed-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[0]));
            return;
        }

        if (!player.hasPermission("lifeserver.speed.other")) {
            messageBuilder.setCommandSender(player).setIDMessage("no-perms").sendMessage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("speed-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[0]));
            return;
        }

        if (Float.parseFloat(args[1]) >= 0 && (Float.parseFloat(args[1]) <= 10)) {
            if (setSpeed(target, Float.valueOf(args[1]))) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("speed-set-other").sendMessage(new TextReplacer().match("%number%").replaceWith(args[1]), new TextReplacer().match("%player_name%").replaceWith(target.getName()));
                return;
            }
            messageBuilder.setCommandSender(commandSender).setIDMessage("walker-set-other").sendMessage(new TextReplacer().match("%number%").replaceWith(args[1]), new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        }
        messageBuilder.setCommandSender(commandSender).setIDMessage("speed-invalid-number-time").sendMessage(new TextReplacer().match("%invalid_number%").replaceWith(args[0]));


    }


    private boolean setSpeed(Player player, Float speed) {
        if (player.isFlying()) {
            player.setFlySpeed(speed / 10);
            messageBuilder.setCommandSender(player).setIDMessage("speed-set").sendMessage(new TextReplacer().match("%number%").replaceWith(String.valueOf(speed)));
            return true;
        }
        player.setWalkSpeed(speed / 10);
        messageBuilder.setCommandSender(player).setIDMessage("walk-set").sendMessage(new TextReplacer().match("%number%").replaceWith(String.valueOf(speed)));
        return false;
    }


}
