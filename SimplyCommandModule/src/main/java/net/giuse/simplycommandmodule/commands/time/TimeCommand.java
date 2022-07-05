package net.giuse.simplycommandmodule.commands.time;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TimeCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public TimeCommand(MainModule mainModule) {
        super("time", "lifeserver.time", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage(simplyCommandService.getMex("time-usage"));
                return;
            }
            if (NumberUtils.isNumber(args[0])) {
                player.getWorld().setTime(Integer.parseInt(args[0]));
                player.sendMessage(simplyCommandService.getMex("time"));
            } else {
                player.sendMessage("Invalid number");
            }
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
