package net.giuse.kitmodule.cooldownsystem;

import lombok.Getter;
import lombok.Setter;
import net.giuse.kitmodule.builder.KitBuilder;


public class KitCooldown  {
    @Getter
    private final KitBuilder kitBuilder;

    @Getter @Setter
    private int variableCoolDown = 0;

    public KitCooldown(KitBuilder kitBuilder) {
        this.kitBuilder = kitBuilder;
    }

    @Override
    public String toString() {
        return kitBuilder.getName() +"_" + variableCoolDown;
    }
}
