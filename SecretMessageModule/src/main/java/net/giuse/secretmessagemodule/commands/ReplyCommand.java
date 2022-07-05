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

        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player sender = (Player) commandSender;

        StringBuilder sb = new StringBuilder();
        SecretChatBuilder secret;
        if (args.length == 0) {
            sender.sendMessage(secretMessageModule.getMessages("insert-text"));
            return;
        }
        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null && secretMessageModule.getSenderSecretChat(sender.getUniqueId()) == null) {
            sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
            return;
        }

        if (secretMessageModule.getReceiverSecretChat(sender.getUniqueId()) == null) {
            secret = secretMessageModule.getSenderSecretChat(sender.getUniqueId());
            if (!secret.getReceiver().isOnline()) {
                sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
                return;
            }
            for (String arg : args) {
                sb.append(arg).append(" ");
            }

            secretChatProcess.setSender(sender);
            secretChatProcess.setReceiver(secret.getReceiver());
        } else {
            secret = secretMessageModule.getReceiverSecretChat(sender.getUniqueId());
            if (!secret.getSender().isOnline()) {
                sender.sendMessage(secretMessageModule.getMessages("nobody-reply"));
                return;
            }

            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            secretChatProcess.setSender(sender);
            secretChatProcess.setReceiver(secret.getSender());
        }

        secretChatProcess.setText(sb.toString());
        secretChatProcess.send();
    }
}

