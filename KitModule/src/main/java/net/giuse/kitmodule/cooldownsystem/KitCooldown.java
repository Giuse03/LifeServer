package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.giuse.kitmodule.builder.KitBuilder;


/*
 * Timer System for each Kit
 */
public class KitCooldown {
    @Getter
    private final KitBuilder kitBuilder;
    @Getter
    @Setter
    private int variableCoolDown = 0;

    public KitCooldown(@NonNull KitBuilder kitBuilder) {
        this.kitBuilder = kitBuilder;
    }

    @Override
    public String toString() {
        return kitBuilder.getName() + "_" + variableCoolDown;
    }
}
