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
            if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(simplyCommandService.getMex("gamemode-available"));
                return;
            }

            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage(simplyCommandService.getMex("gamemode-available"));
                return;
            }
            setGamemode(args[0], player);
        }


        if (!commandSender.hasPermission("lifeserver.gamemode.other")) {
            commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;
        }

        setGamemode(args[0], target.getPlayer());
    }


    /*
     * Gamemode Selector
     */
    private void setGamemode(String gamemode, Player player) {

        if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("1")) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(simplyCommandService.getMex("gamemode").replace("%gamemode%", "creative"));
            return;
        }

        if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("0")) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(simplyCommandService.getMex("gamemode").replace("%gamemode%", "survival"));
            return;
        }

        if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("2")) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(simplyCommandService.getMex("gamemode").replace("%gamemode%", "adventure"));
            return;
        }

        if (gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("3")) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(simplyCommandService.getMex("gamemode").replace("%gamemode%", "spectator"));
            return;
        }
        player.sendMessage(simplyCommandService.getMex("gamemode-available"));
    }
}

