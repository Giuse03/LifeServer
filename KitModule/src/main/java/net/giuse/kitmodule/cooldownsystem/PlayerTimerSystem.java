package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.builder.KitBuilder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

/*
 * Timer System
 */
@RequiredArgsConstructor
@Getter
public class PlayerTimerSystem extends BukkitRunnable {

    private final UUID uuid;
    private final ArrayList<KitCooldown> kitsCooldown = new ArrayList<>();

    /*
     * Run the timer Task
     */
    @Override
    public void run() {
        for (KitCooldown kitCooldown : kitsCooldown) {
            if (kitCooldown.getVariableCoolDown() > 0) {
                kitCooldown.setVariableCoolDown(kitCooldown.getVariableCoolDown() - 1);
            }
        }
    }

    /*
     * Set a cooldown
     */
    public void start(KitCooldown kitCooldown) {
        kitCooldown.setVariableCoolDown(kitCooldown.getKitBuilder().getCoolDown());
    }

    /*
     * Add kit from timer task
     */
    public void addKit(KitBuilder kitBuilder) {
        KitCooldown kitCooldown = new KitCooldown(kitBuilder);
        kitsCooldown.add(kitCooldown);
    }
    
    /*
     * Remove kit from timer task
     */
    public void removeKit(KitBuilder kitBuilder) {
        kitsCooldown.remove(kitsCooldown.stream().filter(kitCooldown -> kitCooldown.getKitBuilder().getName().equalsIgnoreCase(kitBuilder.getName())).findFirst().get());
    }

}