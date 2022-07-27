package net.giuse.simplycommandmodule.commands;


import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class NearCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    @Inject
    public NearCommand(MainModule mainModule) {
        super("near", "lifeserver.near");
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
            return;
        }

        Player player = (Player) commandSender;
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> players = new HashSet<>();

        Bukkit.getOnlinePlayers().stream().filter(other -> other.getLocation().distance(player.getLocation()) <= 10 && !player.equals(other)).forEach(playerNear -> players.add(playerNear.getName()));

        if (players.isEmpty()) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("near-nobody").sendMessage();
            return;
        }

        players.forEach(i -> stringBuilder.append(i).append(","));
        messageBuilder.setCommandSender(commandSender).setIDMessage("near").sendMessage(new TextReplacer().match("%player_list%").replaceWith(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString()));

    }
}
