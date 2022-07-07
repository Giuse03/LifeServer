package net.giuse.kitmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.mainmodule.utils.UtilsItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * KitBuilder for create Concrete Kits
 */


@RequiredArgsConstructor
@Getter
public class KitBuilder {
    private final String name;
    private final int coolDown;
    private String base64;

    public KitBuilder setBase(String base64) {
        this.base64 = base64;
        return this;
    }

    @SneakyThrows
    public void giveItems(Player player) {
        for (ItemStack item : UtilsItemStack.itemStackArrayFromBase64(base64)) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

}