package net.giuse.teleportmodule.commands.teleport;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.subservice.TeleportRequestService;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpDenyCommand extends AbstractCommand {
    private final TeleportModule teleportModule;
    private final TeleportRequestService teleportRequestService;

    @Inject
    public TpDenyCommand(MainModule mainModule) {
        super("tpdeny", "lifeserver.tpdeny", true);
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

        //Check if are Pending Requests
        if (teleportRequestService.getPending(sender.getUniqueId()) == null) {
            sender.sendMessage(teleportModule.getMessage("no-pending-request"));
            return;
        }

        //Deny Pending Requests
        PendingRequest pendingRequest = teleportRequestService.getPending(sender.getUniqueId());
        pendingRequest.getSender().sendMessage(teleportModule.getMessage("request-refused").replace("%playername%", pendingRequest.getReceiver().getName()));
        teleportRequestService.getPendingRequests().remove(pendingRequest);

    }
}
