package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WorkBenchCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public WorkBenchCommand(MainModule mainModule) {
        super("workbench", "lifeserver.workbench", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.openWorkbench(null, true);
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
