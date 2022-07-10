package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.giuse.kitmodule.builder.KitBuilder;


/*
 * Timer System for each Kit
 */
@RequiredArgsConstructor
public class KitCooldown {
    @Getter
    private final String nameKit;
    @Getter
    private final KitBuilder kitBuilder;
    @Getter
    @Setter
    private int variableCoolDown = 0;


    @Override
    public String toString() {
        return nameKit + "_" + variableCoolDown;
    }
}
