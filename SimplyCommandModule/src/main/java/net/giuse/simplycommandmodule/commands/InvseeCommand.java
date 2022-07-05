package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class InvseeCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public InvseeCommand(MainModule mainModule) {
        super("invsee", "lifeserver.invsee", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage(simplyCommandService.getMex("invsee-select-player"));
            } else {

                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    player.openInventory(target.getInventory());
                } else {
                    player.sendMessage(simplyCommandService.getMex("player-not-online"));
                }
            }
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
