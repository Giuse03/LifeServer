package net.giuse.simplycommandmodule.commands.time;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SunCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public SunCommand(MainModule mainModule) {
        super("sun", "lifeserver.sun", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.getWorld().setTime(24000L);
            player.getWorld().setStorm(false);
            player.getWorld().setThundering(false);
            player.sendMessage(simplyCommandService.getMex("time"));
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
