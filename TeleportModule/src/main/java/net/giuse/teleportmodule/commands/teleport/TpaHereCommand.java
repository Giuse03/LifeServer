package net.giuse.teleportmodule.commands.teleport;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.enums.TpType;
import net.giuse.teleportmodule.subservice.TeleportRequestService;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpaHereCommand extends AbstractCommand {
    private final TeleportModule teleportModule;
    private final TeleportRequestService teleportRequestService;

    @Inject
    public TpaHereCommand(MainModule mainModule) {
        super("tpahere", "lifeserver.tpahere", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        teleportRequestService = (TeleportRequestService) mainModule.getService(TeleportRequestService.class);
        setNoPerm(teleportModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported");
            return;
        }
        Player sender = (Player) commandSender;

        //Check if player isn't selected
        if (args.length == 0) {
            sender.sendMessage(("select-player"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        //Check if target is online
        if (target == null) {
            sender.sendMessage(teleportModule.getMessage("player-not-found"));
            return;
        }

        //Send request to the target
        sender.sendMessage(teleportModule.getMessage("tpahere-request-sender").replace("%playername%", target.getName()));
        target.sendMessage(teleportModule.getMessage("tpahere-request-receiver").replace("%playername%", sender.getName()));
        teleportRequestService.getPendingRequests().add(new PendingRequest(sender, target, TpType.TPA_HERE));
    }
}
