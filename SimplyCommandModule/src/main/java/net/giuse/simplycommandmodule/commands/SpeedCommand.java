package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SpeedCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;
    @Inject
    public SpeedCommand(MainModule mainModule) {
        super("speed", "lifeserver.speed", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            return;
        }
        Player player = (Player) commandSender;
        if (args.length == 0) {
            player.sendMessage(simplyCommandService.getMex("speed-usage"));
            return;
        }
        if (args.length == 1) {
            if (!NumberUtils.isNumber(args[0])) {
                player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));
                return;
            }

            if (Integer.parseInt(args[0]) >= 0 && (Integer.parseInt(args[0]) <= 10)) {
                setSpeed(player, Float.valueOf(args[0]));
                return;
            }

            player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));
            return;
        }

        if (!player.hasPermission("lifeserver.speed.other")) {
            player.sendMessage(simplyCommandService.getMex("no-perms"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;
        }

        if (!NumberUtils.isNumber(args[1])) {
            player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));
            return;
        }

        if (Integer.parseInt(args[1]) >= 0 && (Integer.parseInt(args[1]) <= 10)) {
            if (setSpeed(target, Float.valueOf(args[1]))) {
                player.sendMessage(simplyCommandService.getMex("speed-set-other").replace("%player_name%", target.getName()).replace("%number%", args[1]));
                return;
            }
            player.sendMessage(simplyCommandService.getMex("walk-set-other").replace("%player_name%", target.getName()).replace("%number%", args[1]));
        }
        player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));


    }


    private boolean setSpeed(Player player, Float speed) {
        if (player.isFlying()) {
            player.setFlySpeed(speed / 10);
            player.sendMessage(simplyCommandService.getMex("speed-set").replace("%number%", String.valueOf(speed)));
            return true;
        }
        player.setWalkSpeed(speed / 10);
        player.sendMessage(simplyCommandService.getMex("walk-set").replace("%number%", String.valueOf(speed)));
        return false;
    }


}
