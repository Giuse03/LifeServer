package net.giuse.secretmessagemodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.SecretChatBuilder;
import net.giuse.secretmessagemodule.SecretMessageModule;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ReplyCommand extends AbstractCommand {


    private final SecretMessageModule secretMessageModule;

    private final SecretChatProcess secretChatProcess;

    @Inject
    public ReplyCommand(MainModule mainModule) {
        super("reply", "lifeserver.reply", true);
        secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
        secretChatProcess = mainModule.getInjector().getSingleton(SecretChatProcess.class);
        setNoPerm(secretMessageModule.getMessages("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }

        Player sender = (Player) commandSender;
        StringBuilder sb = new StringBuilder();
        SecretChatBuilder secret;

        //Check if text lenght is 0
        if (args.length == 0) {
            sender.sendMessage(secretMessageModule.getMessages("insert-text"));
            return;
        }

        //Check if exists receiver
        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null && secretMessageModule.getSenderSecretChat(sender.getUniqueId()) == null) {
            sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
            return;
        }

        //Search between sender a player to reply
        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null) {
            secret = secretMessageModule.getSenderSecretChat(sender.getUniqueId());

            //Check if Receiver isn't Online
            if (!secret.getReceiver().isOnline()) {
                sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
                return;
            }

            //Build Message
            for (String arg : args) {
                sb.append(arg).append(" ");
            }

            //Set Sender and Receiver
            secretChatProcess.setSender(sender);
            secretChatProcess.setReceiver(secret.getReceiver());

            //Send Message
            secretChatProcess.setText(sb.toString());
            secretChatProcess.send();
            return;
        }

        //Check if Receiver isn't Online
        secret = secretMessageModule.getReceiverSecretChat(sender.getUniqueId());
        if (!secret.getSender().isOnline()) {
            sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
            return;
        }

        //Build Message
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        //Set Sender and Receiver
        secretChatProcess.setSender(sender);
        secretChatProcess.setReceiver(secret.getSender());

        //Send Message
        secretChatProcess.setText(sb.toString());
        secretChatProcess.send();
    }
}

