package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SudoCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;
    @Inject
    public SudoCommand(MainModule mainModule) {
        super("sudo", "lifeserver.sudo", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            commandSender.sendMessage(simplyCommandService.getMex("sudo-usage"));
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;

        }
        target.performCommand(sb.toString());
        commandSender.sendMessage(simplyCommandService.getMex("sudo-forced").replace("%command%", sb.toString()).replace("%player_name%", target.getName()));

    }
}
