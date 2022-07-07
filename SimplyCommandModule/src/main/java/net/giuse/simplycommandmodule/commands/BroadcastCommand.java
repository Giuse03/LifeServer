package net.giuse.simplycommandmodule.commands;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

public class BroadcastCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public BroadcastCommand(MainModule mainModule) {
        super("broadcast", "lifeserver.broadcast", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(simplyCommandService.getMex("broadcast-usage"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayers -> onlinePlayers.sendMessage(simplyCommandService.getMex("broadcast").replace("%message%", sb.toString())));

    }
}
