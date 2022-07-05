package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class FlyCommand extends AbstractCommand {


    private final SimplyCommandService simplyCommandService;

    @Inject
    public FlyCommand(MainModule mainModule) {
        super("fly", "lifeserver.fly", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.isFlying()) {
                    player.sendMessage(simplyCommandService.getMex("fly-disabled"));
                    player.setAllowFlight(false);
                    player.setFlying(false);
                } else {
                    player.sendMessage(simplyCommandService.getMex("fly-enable"));
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
            } else if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            }
        } else {
            if (commandSender.hasPermission("lifeserver.fly.other")) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target.isFlying()) {
                        target.setAllowFlight(false);
                        target.setFlying(false);
                        commandSender.sendMessage(simplyCommandService.getMex("fly-disabled-other").replace("%player_name%", target.getName()));
                        target.sendMessage(simplyCommandService.getMex("fly-disabled"));
                    } else {
                        target.setAllowFlight(true);
                        target.setFlying(true);
                        commandSender.sendMessage(simplyCommandService.getMex("fly-enable-other").replace("%player_name%", target.getName()));
                        target.sendMessage(simplyCommandService.getMex("fly-enable"));
                    }
                } else {
                    commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
                }
            } else {
                commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            }
        }
    }
}
