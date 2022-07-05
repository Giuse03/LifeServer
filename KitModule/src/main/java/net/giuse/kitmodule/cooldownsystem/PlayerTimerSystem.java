package net.giuse.kitmodule.cooldownsystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/*
 * Timer System
 */

@AllArgsConstructor
public class PlayerTimerSystem extends BukkitRunnable {
    @Getter
    private final String nameKit;
    @Getter
    private final UUID uuid;
    private final int fixedCoolDown;
    @Getter
    private int variableCoolDown;

    @Override
    public void run() {
        if (variableCoolDown > 0) variableCoolDown = variableCoolDown - 1;
    }

    public void start() {
        variableCoolDown = fixedCoolDown;
    }
}