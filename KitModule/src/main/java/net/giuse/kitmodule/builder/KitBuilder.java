package net.giuse.kitmodule.builder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * KitBuilder for create Concrete Kits
 */

@Setter
@Getter
public class KitBuilder {
    private final ArrayList<String> items = new ArrayList<>();
    private String name;
    private int coolDown;

    @Override
    public String toString() {
        return "name: " + name + " cooldown:" + coolDown + " Items:" + items;
    }

}