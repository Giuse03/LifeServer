package net.giuse.teleportmodule.commands.spawn;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import net.giuse.teleportmodule.subservice.SpawnLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class SetSpawnCommand extends AbstractCommand {
    private final SpawnLoaderService spawnLoaderService;
    private final TeleportModule teleportModule;

    @Inject
    public SetSpawnCommand(MainModule mainModule) {
        super("setspawn", "lifeserver.setspawn", true);
        spawnLoaderService = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        setNoPerm(teleportModule.getMessage("no-perms"));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        //Check if sender is Console
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not Supported From Console");
            return;
        }

        //Set Spawn
        Player player = (Player) commandSender;
        spawnLoaderService.setSpawnBuilder(new SpawnBuilder(player.getLocation()));
        commandSender.sendMessage(teleportModule.getMessage("setspawn"));
    }
}
