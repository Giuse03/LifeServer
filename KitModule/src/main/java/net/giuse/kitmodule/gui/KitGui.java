package net.giuse.kitmodule.gui;


import eu.giuse.inventorylib.InventoryBuilder;
import lombok.Getter;
import net.giuse.engine.Worker;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Initialize Kit Gui
 */

public class KitGui implements GuiInitializer {
    @Getter
    private InventoryBuilder inventoryBuilder;
    @Inject
    private MainModule mainModule;
    @Inject
    private Worker worker;
    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                kitModule.getConfigManager().getKitYaml().getInt("inventory.rows"),
                kitModule.getConfigManager().getKitYaml().getString("inventory.title"),
                kitModule.getConfigManager().getKitYaml().getInt("inventory.page"),
                worker).createInvs();

        //Initialize items
        mainModule.getInjector().getSingleton(NextItemGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(PreviousItemGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(ItemsGuiInit.class).initItems(inventoryBuilder);

        //Build InventoryBuilder
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }

    /*
     * Open Inventory to a Player
     */
    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getInventoryHash().get(1));
    }
}
