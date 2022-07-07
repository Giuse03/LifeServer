package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class EnderchestCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public EnderchestCommand(MainModule mainModule) {
        super("enderchest", "lifeserver.enderchest", false);
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
            player.openInventory(player.getEnderChest());
            return;
        }

        if (!player.hasPermission("lifeserver.enderchest.other")) {
            commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;
        }
        player.openInventory(target.getEnderChest());
    }
}
