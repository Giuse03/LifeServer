package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class GamemodeCommand extends AbstractCommand {

    private final SimplyCommandService simplyCommandService;

    @Inject
    public GamemodeCommand(MainModule mainModule) {
        super("gamemode", "lifeserver.gamemode", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0 || args.length == 1) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (args.length == 0) {
                    player.sendMessage(simplyCommandService.getMex("gamemode-available"));
                } else {
                    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(simplyCommandService.getMex("gamemode")
                                .replace("%gamemode%", "creative"));
                    } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(simplyCommandService.getMex("gamemode")
                                .replace("%gamemode%", "survival"));
                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                        player.setGameMode(GameMode.ADVENTURE);
                        player.sendMessage(simplyCommandService.getMex("gamemode")
                                .replace("%gamemode%", "adventure"));
                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                        player.setGameMode(GameMode.SPECTATOR);
                        player.sendMessage(simplyCommandService.getMex("gamemode")
                                .replace("%gamemode%", "spectator"));
                    } else {
                        player.sendMessage(simplyCommandService.getMex("gamemode-available"));
                    }
                }
            } else if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(simplyCommandService.getMex("gamemode-available"));
            }
        } else {
            if (commandSender.hasPermission("lifeserver.gamemode.other")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (target.getName().equalsIgnoreCase(args[1])) {
                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                            target.setGameMode(GameMode.CREATIVE);
                            commandSender.sendMessage(simplyCommandService.getMex("gamemode-other")
                                    .replace("%player_name%", target.getName())
                                    .replace("%gamemode%", "CREATIVE"));
                            target.sendMessage(simplyCommandService.getMex("gamemode")
                                    .replace("%gamemode%", "CREATIVE"));
                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                            target.setGameMode(GameMode.SURVIVAL);
                            commandSender.sendMessage(simplyCommandService.getMex("gamemode-other")
                                    .replace("%player_name%", target.getName())
                                    .replace("%gamemode%", "SURVIVAL"));
                            target.sendMessage(simplyCommandService.getMex("gamemode")
                                    .replace("%gamemode%", "SURVIVAL"));
                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                            target.setGameMode(GameMode.ADVENTURE);
                            commandSender.sendMessage(simplyCommandService.getMex("gamemode-other")
                                    .replace("%player_name%", target.getName())
                                    .replace("%gamemode%", "ADVENTURE"));
                            target.sendMessage(simplyCommandService.getMex("gamemode")
                                    .replace("%gamemode%", "ADVENTURE"));
                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                            target.setGameMode(GameMode.SPECTATOR);
                            commandSender.sendMessage(simplyCommandService.getMex("gamemode-other")
                                    .replace("%player_name%", target.getName())
                                    .replace("%gamemode%", "SPECTATOR"));
                            target.sendMessage(simplyCommandService.getMex("gamemode")
                                    .replace("%gamemode%", "SPECTATOR"));
                        } else {
                            commandSender.sendMessage(simplyCommandService.getMex("gamemode-available"));

                        }

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

