package net.giuse.simplycommandmodule.commands.time;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class NightCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public NightCommand(MainModule mainModule) {
        super("night", "lifeserver.night", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            return;
        }
        Player player = (Player) commandSender;
        player.getWorld().setTime(18000L);
        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);
        player.sendMessage(simplyCommandService.getMex("time"));
    }
}
