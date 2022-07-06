package net.giuse.kitmodule.cooldownsystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.builder.KitBuilder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/*
 * Timer System
 */
@RequiredArgsConstructor @Getter
public class PlayerTimerSystem extends BukkitRunnable {

    private final UUID uuid;
    private final ArrayList<KitCooldown> kitsCooldown = new ArrayList<>();

    @Override
    public void run() {
        for (KitCooldown kitCooldown : kitsCooldown){
            if (kitCooldown.getVariableCoolDown() > 0){
                kitCooldown.setVariableCoolDown(kitCooldown.getVariableCoolDown() - 1);
            }
        }
    }

    public void start(KitCooldown kitCooldown) {
        kitCooldown.setVariableCoolDown(kitCooldown.getKitBuilder().getCoolDown());
    }

    public void addKit(KitBuilder kitBuilder) {
        KitCooldown kitCooldown = new KitCooldown(kitBuilder);
        kitsCooldown.add(kitCooldown);
    }

}