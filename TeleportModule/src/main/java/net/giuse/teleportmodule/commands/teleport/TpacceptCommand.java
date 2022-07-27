package net.giuse.teleportmodule.commands.teleport;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
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
    private final MessageBuilder messageBuilder;
    private final TeleportRequestService teleportRequestService;
    private final TeleportModule teleportModule;

    @Inject
    public TpacceptCommand(MainModule mainModule) {
        super("tpaccept", "lifeserver.tpaccept");
        messageBuilder = mainModule.getMessageBuilder();
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        teleportRequestService = (TeleportRequestService) mainModule.getService(TeleportRequestService.class);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }
        Player player = (Player) commandSender;


        //Check if there is any Pending Request
        if (teleportRequestService.getPending(player.getUniqueId()) == null) {
            messageBuilder.setCommandSender(player).setIDMessage("no-pending-request").sendMessage();
            return;
        }

        //Check the type of PendingRequest
        PendingRequest pendingRequest = teleportRequestService.getPending(player.getUniqueId());
        if (pendingRequest.getTpType().equals(TpType.TPA)) {
            teleportModule.getBackLocations().put(pendingRequest.getSender(), pendingRequest.getSender().getLocation());
            PaperLib.teleportAsync(pendingRequest.getSender(), pendingRequest.getReceiver().getLocation());
        } else {
            teleportModule.getBackLocations().put(pendingRequest.getReceiver(), pendingRequest.getReceiver().getLocation());
            PaperLib.teleportAsync(pendingRequest.getReceiver(), pendingRequest.getSender().getLocation());
        }
        //Accept Pending Request
        messageBuilder.setCommandSender(pendingRequest.getSender()).setIDMessage("teleport-player").sendMessage(new TextReplacer().match("%playername%").replaceWith(pendingRequest.getReceiver().getName()));
        messageBuilder.setCommandSender(player).setIDMessage("request-accept-receiver").sendMessage(new TextReplacer().match("%playername%").replaceWith(player.getName()));

        teleportRequestService.getPendingRequests().remove(pendingRequest);

    }
}
