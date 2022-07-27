package inventorylib;

import inventorylib.inventoryevents.ClickEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class ButtonBuilder implements Listener {
    private final InventoryBuilder inventoryBuilder;
    private final int position,page;

    private final ItemStack itemStack;
    private final boolean nextPage, previousPage, eventCancelled;
    @Setter
    private ClickEvent event;

    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getSlot() == position && inventoryBuilder.getInventoryHash().get(page).equals(inventoryClickEvent.getInventory())) {
            if (nextPage && inventoryBuilder.getNPage() > 1)
                inventoryBuilder.nextPage((Player) inventoryClickEvent.getWhoClicked());
            if (previousPage && inventoryBuilder.getNPage() > 1)
                inventoryBuilder.previousPage((Player) inventoryClickEvent.getWhoClicked());
            if (eventCancelled) inventoryClickEvent.setCancelled(true);
            event.click(inventoryClickEvent);
        }
    }
}
