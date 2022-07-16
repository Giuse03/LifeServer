package net.giuse.kitmodule.cooldownsystem;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * Timer System
 */
@RequiredArgsConstructor
@Getter
public class PlayerKitCooldown extends BukkitRunnable {
    private final Object2IntArrayMap<String> coolDownKits = new Object2IntArrayMap<>();

    /*
     * Run the timer Task
     */
    @Override
    public void run() {
        coolDownKits.keySet().forEach(coolDownKitsName -> {
            boolean isInCoolDown = coolDownKits.getInt(coolDownKitsName) > 0;
            if (isInCoolDown) {
                int newCoolDown = coolDownKits.getInt(coolDownKitsName) - 1;
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
        coolDownKits.removeInt(name);
    }

    /*
     * Remove kit from timer task
     */
    public int getActualCooldown(String name) {
        return coolDownKits.getInt(name);
    }

    public int getSizeKitCooldown() {
        return coolDownKits.size();
    }
}