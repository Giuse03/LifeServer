package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HealCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;


    @Inject
    public HealCommand(MainModule mainModule) {
        super("heal", "lifeserver.heal", false);
        this.simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(simplyCommandService.getMex("not-player"));
                return;
            }
            Player player = (Player) commandSender;
            player.setHealth(20);
            player.sendMessage(simplyCommandService.getMex("heal"));
            return;
        }

        if (!commandSender.hasPermission("lifeserver.heal.other")) {
            commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;
        }

        target.sendMessage(simplyCommandService.getMex("heal"));
        commandSender.sendMessage(simplyCommandService.getMex("heal-other").replace("%player_name%", target.getName()));
        target.setHealth(20);
    }

}
