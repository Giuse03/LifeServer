package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class FoodCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public FoodCommand(MainModule mainModule) {
        super("food", "lifeserver.food", false);
        this.simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            return;
        }
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.setFoodLevel(20);
            player.sendMessage(simplyCommandService.getMex("food"));
            return;
        }

        if (!commandSender.hasPermission("lifeserver.food.other")) {
            commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            commandSender.sendMessage(simplyCommandService.getMex("player-not-online"));
            return;
        }

        target.sendMessage(simplyCommandService.getMex("food"));
        commandSender.sendMessage(simplyCommandService.getMex("food-other").replace("%player_name%", target.getName()));
        target.setFoodLevel(20);
    }
}
