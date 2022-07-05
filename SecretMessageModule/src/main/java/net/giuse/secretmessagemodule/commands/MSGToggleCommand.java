package net.giuse.secretmessagemodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class MSGToggleCommand extends AbstractCommand {
    private final SecretMessageModule secretMessageModule;

    @Inject
    public MSGToggleCommand(MainModule mainModule) {
        super("msgtoggle", "lifeserver.msgtoggle", true);
        secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
        setNoPerm(secretMessageModule.getMessages("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player player = (Player) commandSender;
        if (secretMessageModule.getPlayerMsgToggle().contains(player)) {
            player.sendMessage(secretMessageModule.getMessages("msgtoggle").replace("%status%", "§cOFF"));
            secretMessageModule.getPlayerMsgToggle().remove(player);
        } else {
            player.sendMessage(secretMessageModule.getMessages("msgtoggle").replace("%status%", "§aON"));
            secretMessageModule.getPlayerMsgToggle().add(player);

        }
    }
}
