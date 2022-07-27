package net.giuse.teleportmodule.commands.spawn;

import ezmessage.MessageBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.subservice.SpawnLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.inject.Inject;

public class DelSpawnCommand extends AbstractCommand {
    private final SpawnLoaderService spawnLoaderService;
    private final MessageBuilder messageBuilder;

    @Inject
    public DelSpawnCommand(MainModule mainModule) {
        super("delspawn", "lifeserver.delspawn");
        spawnLoaderService = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        //Delete Spawn
        spawnLoaderService.setSpawnBuilder(null);
        messageBuilder.setCommandSender(commandSender).setIDMessage("removespawn").sendMessage();
    }
}
