package net.giuse.teleportmodule.commands.teleport;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.subservice.TeleportRequestService;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class TpDenyCommand extends AbstractCommand {
    private final MessageBuilder messageBuilder;
    private final TeleportRequestService teleportRequestService;

    @Inject
    public TpDenyCommand(MainModule mainModule) {
        super("tpdeny", "lifeserver.tpdeny");
        messageBuilder = mainModule.getMessageBuilder();
        teleportRequestService = (TeleportRequestService) mainModule.getService(TeleportRequestService.class);
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
            messageBuilder.setCommandSender(sender).setIDMessage("no-pending-request").sendMessage();
            return;
        }

        //Deny Pending Requests
        PendingRequest pendingRequest = teleportRequestService.getPending(sender.getUniqueId());
        messageBuilder.setCommandSender(pendingRequest.getSender()).setIDMessage("request-refused").sendMessage(new TextReplacer().match("%playername%").replaceWith(pendingRequest.getReceiver().getName()));
        teleportRequestService.getPendingRequests().remove(pendingRequest);

    }
}
