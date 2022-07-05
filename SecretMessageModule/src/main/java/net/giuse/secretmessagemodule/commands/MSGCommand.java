package net.giuse.secretmessagemodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.SecretMessageModule;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;


public class MSGCommand extends AbstractCommand {


    private final SecretMessageModule secretMessageModule;

    private final SecretChatProcess secretChatProcess;

    @Inject
    public MSGCommand(MainModule mainModule) {
        super("msg", "lifeserver.msg", true);
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
        if (args.length == 0) {
            sender.sendMessage(secretMessageModule.getMessages("usage"));
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(secretMessageModule.getMessages("insert-text"));
            return;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(secretMessageModule.getMessages("player-offline"));
            return;
        }
        Player receiver = Bukkit.getPlayer(args[0]);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        secretChatProcess.setSender(sender);
        secretChatProcess.setReceiver(receiver);
        secretChatProcess.setText(sb.toString());
        secretChatProcess.send();
    }
}
