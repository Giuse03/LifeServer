package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/*
 * Timer System
 */
@RequiredArgsConstructor
@Getter
public class PlayerKitCooldown extends BukkitRunnable {
    private final HashMap<String, Integer> coolDownKits = new HashMap<>();


    /*
     * Run the timer Task
     */
    @Override
    public void run() {
        coolDownKits.keySet().forEach(coolDownKitsName -> {
            boolean isInCoolDown = coolDownKits.get(coolDownKitsName) > 0;
            if (isInCoolDown) {
                int newCoolDown = coolDownKits.get(coolDownKitsName) - 1;
                coolDownKits.put(coolDownKitsName, newCoolDown);
            }
        });
    }

    /*
     * Add kit from timer task
     */
    public void addKit(String name, int kitCooldown) {
        coolDownKits.put(name, kitCooldown);

    }

    /*
     * Remove kit from timer task
     */
    public void removeKit(String name) {
        coolDownKits.remove(name);
    }

    /*
     * Remove kit from timer task
     */
    public int getActualCooldown(String name) {
        if (!coolDownKits.containsKey(name)) return 0;
        return coolDownKits.get(name);
    }

    public int getSizeKitCooldown() {
        return coolDownKits.size();
    }
}