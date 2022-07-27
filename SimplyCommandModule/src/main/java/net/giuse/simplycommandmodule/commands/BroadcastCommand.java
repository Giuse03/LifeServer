package net.giuse.simplycommandmodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

public class BroadcastCommand extends AbstractCommand {

    private final MessageBuilder messageBuilder;

    @Inject
    public BroadcastCommand(MainModule mainModule) {
        super("broadcast", "lifeserver.broadcast");
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            messageBuilder.setCommandSender(commandSender).setIDMessage("broadcast-usage").sendMessage();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayers -> messageBuilder.setCommandSender(commandSender).setIDMessage("broadcast").sendMessage(new TextReplacer().match("%message%").replaceWith(sb.toString())));

    }
}
