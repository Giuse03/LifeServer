package net.giuse.secretmessagemodule.commands;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class MSGToggleCommand extends AbstractCommand {
    private final SecretMessageModule secretMessageModule;
    private final MessageBuilder messageBuilder;

    @Inject
    public MSGToggleCommand(MainModule mainModule) {
        super("msgtoggle", "lifeserver.msgtoggle");
        secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player player = (Player) commandSender;

        //Enable or disable MSG-Toggle
        if (secretMessageModule.getPlayerMsgToggle().contains(player)) {
            messageBuilder.setCommandSender(player).setIDMessage("msgtoggle").sendMessage(new TextReplacer().match("%status%").replaceWith("§cOFF"));
            secretMessageModule.getPlayerMsgToggle().remove(player);
        } else {
            messageBuilder.setCommandSender(player).setIDMessage("msgtoggle").sendMessage(new TextReplacer().match("%status%").replaceWith("§aON"));
            secretMessageModule.getPlayerMsgToggle().add(player);

        }
    }
}
