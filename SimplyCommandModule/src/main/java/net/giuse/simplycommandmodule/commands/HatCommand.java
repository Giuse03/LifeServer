package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HatCommand extends AbstractCommand {

    private final SimplyCommandService simplyCommandService;

    @Inject
    public HatCommand(MainModule mainModule) {
        super("hat", "lifeserver.hat", false);
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
        if (player.getInventory().getItemInMainHand() != null) {
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(null);
        }
    }
}
