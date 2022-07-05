package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ClearInventoryCommand extends AbstractCommand {


    private final SimplyCommandService simplyCommandService;

    @Inject
    public ClearInventoryCommand(MainModule mainModule) {
        super("clearinventory", "lifeserver.clearinventory", false);
        this.simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.sendMessage(simplyCommandService.getMex("cleaninv"));
            } else if (commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage(simplyCommandService.getMex("not-player"));

            }
        } else {
            if (commandSender.hasPermission("lifeserver.clearinventory.other")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    target.getInventory().clear();
                    target.getInventory().setArmorContents(null);
                    commandSender.sendMessage(simplyCommandService.getMex("cleaninv-other").replace("%player_name%", target.getName()));
                    target.sendMessage(simplyCommandService.getMex("cleaninv"));
                } else {
                    commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
                }
            } else {
                commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            }
        }
    }
}



