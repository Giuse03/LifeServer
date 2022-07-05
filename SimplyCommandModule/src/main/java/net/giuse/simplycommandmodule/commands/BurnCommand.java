package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class BurnCommand extends AbstractCommand {

    private final SimplyCommandService simplyCommandService;

    @Inject
    public BurnCommand(MainModule mainModule) {
        super("burn", "lifeserver.burn", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.setFireTicks(20 * 20);
                player.sendMessage(simplyCommandService.getMex("burning").replace("%player_name%", player.getName()).replace("%seconds%", "20"));
            } else {
                commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setFireTicks(20 * 20);
                commandSender.sendMessage(simplyCommandService.getMex("burning").replace("%player_name%", target.getName()).replace("%seconds%", "20"));
            } else {
                commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
            }
        } else if (args.length == 2) {
            if (commandSender.hasPermission("lifeserver.burn.other")) {
                if (NumberUtils.isNumber(args[0])) {
                    if (Integer.parseInt(args[0]) >= 0 && (Integer.parseInt(args[0]) <= 10)) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            target.setFireTicks(Integer.parseInt(args[1]));
                            commandSender.sendMessage(simplyCommandService.getMex("burning").replace("%player_name%", target.getName()).replace("%seconds%", args[1]));
                        } else {
                            commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
                        }
                    } else {
                        commandSender.sendMessage(simplyCommandService.getMex("burn-invalid-number-time"));
                    }
                } else {
                    commandSender.sendMessage(simplyCommandService.getMex("burn-invalid-number-time"));
                }
            } else {
                commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            }
        }
    }
}