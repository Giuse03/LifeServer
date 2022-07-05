package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.hasPermission("lifeserver.speed")) {
                if (args.length == 0) {
                    player.sendMessage(simplyCommandService.getMex("speed-usage"));
                } else if (args.length == 1) {
                    if (NumberUtils.isNumber(args[0])) {
                        if (Integer.parseInt(args[0]) >= 0 && (Integer.parseInt(args[0]) <= 10)) {
                            setSpeed(player, Float.valueOf(args[0]));
                        } else {
                            player.sendMessage(simplyCommandService.getMex("burn-invalid-number-time").replace("%invalid_number%", args[0]));
                        }
                    } else {
                        player.sendMessage(simplyCommandService.getMex("burn-invalid-number-time").replace("%invalid_number%", args[0]));
                    }
                } else {
                    if (player.hasPermission("lifeserver.speed.other")) {
                        if (Bukkit.getPlayer(args[0]) != null) {
                            if (NumberUtils.isNumber(args[1])) {
                                if (Integer.parseInt(args[1]) >= 0 && (Integer.parseInt(args[1]) <= 10)) {
                                    setSpeed(player, Bukkit.getPlayer(args[0]), Float.valueOf(args[1]));
                                } else {
                                    player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));
                                }
                            } else {
                                player.sendMessage(simplyCommandService.getMex("speed-invalid-number-time").replace("%invalid_number%", args[0]));
                            }
                        } else {
                            simplyCommandService.getMex("player-not-online");
                        }
                    }

                }
            }
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }


    private void setSpeed(Player player, Float speed) {
        if (player.isFlying()) {
            player.setFlySpeed(speed / 10);
            player.sendMessage(simplyCommandService.getMex("speed-set").replace("%number%", String.valueOf(speed)));
        } else {
            player.setWalkSpeed(speed / 10);
            player.sendMessage(simplyCommandService.getMex("walk-set").replace("%number%", String.valueOf(speed)));

        }
    }


    private void setSpeed(Player player, Player target, Float speed) {
        if (target.isFlying()) {
            target.setFlySpeed(speed / 10);
            target.sendMessage(simplyCommandService.getMex("speed-set").replace("%number%", String.valueOf(speed)));
            player.sendMessage(simplyCommandService.getMex("speed-set-other").replace("%player_name%", target.getName()).replace("%number%", String.valueOf(speed)));

        } else {
            target.setWalkSpeed(speed / 10);
            target.sendMessage(simplyCommandService.getMex("walk-set").replace("%number%", String.valueOf(speed)));
            player.sendMessage(simplyCommandService.getMex("walk-set-other").replace("%player_name%", target.getName()).replace("%number%", String.valueOf(speed)));

        }
    }

}
