package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.builder.KitBuilder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/*
 * Timer System
 */
@RequiredArgsConstructor
@Getter
public class PlayerTimerSystem extends BukkitRunnable {
    private final ArrayList<KitCooldown> kitsCooldown = new ArrayList<>();

    /*
     * Run the timer Task
     */
    @Override
    public void run() {
        kitsCooldown.stream().filter(kitCooldown -> kitCooldown.getVariableCoolDown() > 0).forEach(kitCooldown -> kitCooldown.setVariableCoolDown(kitCooldown.getVariableCoolDown() - 1));
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
    public void addKit(String name,KitBuilder kitBuilder) {
        KitCooldown kitCooldown = new KitCooldown(name,kitBuilder);
        kitsCooldown.add(kitCooldown);
    }

    /*
     * Remove kit from timer task
     */
    public void removeKit(String name) {
        kitsCooldown.remove(kitsCooldown.stream().filter(kitCooldown -> kitCooldown.getNameKit().equalsIgnoreCase(name)).findFirst().get());
    }

}