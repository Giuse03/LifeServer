package net.giuse.secretmessagemodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SocialSpyCommand extends AbstractCommand {
    private final SecretMessageModule secretMessageModule;

    @Inject
    public SocialSpyCommand(MainModule mainModule) {
        super("socialspy", "lifeserver.socialspy", true);
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
        if (secretMessageModule.getPlayerSocialSpy().contains(player)) {
            player.sendMessage(secretMessageModule.getMessages("socialspy").replace("%status%", "§cOFF"));
            secretMessageModule.getPlayerSocialSpy().remove(player);
        } else {
            player.sendMessage(secretMessageModule.getMessages("socialspy").replace("%status%", "§aON"));
            secretMessageModule.getPlayerSocialSpy().add(player);

        }

    }
}
