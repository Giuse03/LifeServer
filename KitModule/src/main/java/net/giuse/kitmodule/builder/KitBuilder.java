package net.giuse.kitmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * KitBuilder for create Concrete Kits
 */


@RequiredArgsConstructor
@Getter
public class KitBuilder {

    private final int coolDown;
    private String elementsKitBase64;
    private ItemStack[] itemStackList;

    public KitBuilder setBase(String elementsKitBase64) {
        this.elementsKitBase64 = elementsKitBase64;
        return this;
    }

    public void build() {
        itemStackList = Utils.itemStackArrayFromBase64(elementsKitBase64);
    }

    @SneakyThrows
    public void giveItems(Player player) {
        for (ItemStack item : itemStackList) {

            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }
        }
    }

}