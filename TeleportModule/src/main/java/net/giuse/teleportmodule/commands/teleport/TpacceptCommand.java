package net.giuse.teleportmodule.commands.teleport;

import io.papermc.lib.PaperLib;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.enums.TpType;
import net.giuse.teleportmodule.subservice.TeleportRequestService;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpacceptCommand extends AbstractCommand {

    private final TeleportModule teleportModule;

    private final TeleportRequestService teleportRequestService;

    @Inject
    public TpacceptCommand(MainModule mainModule) {
        super("tpaccept", "lifeserver.tpaccept", true);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        teleportRequestService = (TeleportRequestService) mainModule.getService(TeleportRequestService.class);
        setNoPerm(teleportModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;
        if (teleportRequestService.getPending(player.getUniqueId()) == null) {
            player.sendMessage(teleportModule.getMessage("no-pending-request"));
            return;
        }

        PendingRequest pendingRequest = teleportRequestService.getPending(player.getUniqueId());
        if (pendingRequest.getTpType().equals(TpType.TPA)) {
            teleportModule.getBackLocations().put(pendingRequest.getSender(), pendingRequest.getSender().getLocation());
            PaperLib.teleportAsync(pendingRequest.getSender(), pendingRequest.getReceiver().getLocation());
        } else {
            teleportModule.getBackLocations().put(pendingRequest.getReceiver(), pendingRequest.getReceiver().getLocation());
            PaperLib.teleportAsync(pendingRequest.getReceiver(), pendingRequest.getSender().getLocation());
        }
        pendingRequest.getSender().sendMessage(teleportModule.getMessage("teleport-player").replace("%playername%", pendingRequest.getReceiver().getName()));
        player.sendMessage(teleportModule.getMessage("request-accept-receiver").replace("%playername%", player.getName()));
        teleportRequestService.getPendingRequests().remove(pendingRequest);

    }
}
