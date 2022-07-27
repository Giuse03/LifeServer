package net.giuse.simplycommandmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class GodCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;

    private final SimplyCommandService simplyCommandService;

    @Inject
    public GodCommand(MainModule mainModule) {
        super("god", "lifeserver.god");
        messageBuilder = mainModule.getMessageBuilder();
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("not-player").sendMessage();
                return;
            }

            Player player = (Player) commandSender;
            if (simplyCommandService.getStringsNameGods().contains(player.getName())) {
                messageBuilder.setCommandSender(commandSender).setIDMessage("god-disabled").sendMessage();
                simplyCommandService.getStringsNameGods().remove(player.getName());
                return;
            }

            messageBuilder.setCommandSender(commandSender).setIDMessage("god-enabled").sendMessage();
            simplyCommandService.getStringsNameGods().add(player.getName());
            return;


        }

        if (!commandSender.hasPermission("lifeserver.god.other")) {
            commandSender.sendMessage("No Perms");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("player-not-online").sendMessage();
            return;
        }

        if (simplyCommandService.getStringsNameGods().contains(target.getName())) {
            messageBuilder.setCommandSender(target).setIDMessage("god-disabled").sendMessage();
            messageBuilder.setCommandSender(commandSender).setIDMessage("god-disabled-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
            simplyCommandService.getStringsNameGods().remove(target.getName());
            return;
        }

        messageBuilder.setCommandSender(target).setIDMessage("god-enabled").sendMessage();
        messageBuilder.setCommandSender(commandSender).setIDMessage("god-enabled-other").sendMessage(new TextReplacer().match("%player_name%").replaceWith(target.getName()));
        simplyCommandService.getStringsNameGods().add(target.getName());
    }


}
