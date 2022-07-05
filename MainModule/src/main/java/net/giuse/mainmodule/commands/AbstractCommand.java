package net.giuse.mainmodule.commands;

import lombok.Getter;
import lombok.Setter;
import net.giuse.engine.Worker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;


/**
 * AbstractCommand for create Concrete Commands
 */

@Getter
public abstract class AbstractCommand extends Command {
    private final String permission;
    private final boolean async;
    @Setter
    private String noPerm;

    public AbstractCommand(String name, String permission, boolean async) {
        super(name);
        this.permission = permission;
        this.async = async;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!sender.hasPermission(this.permission)) {
            sender.sendMessage(noPerm);
            return true;
        }
        Worker.executeProcess(CompletableFuture.supplyAsync(() -> () -> execute(sender, args)), async);
        return true;
    }

    public abstract void execute(CommandSender commandSender, String[] args);


}