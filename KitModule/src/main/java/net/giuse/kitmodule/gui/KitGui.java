package net.giuse.kitmodule.gui;


import eu.giuse.inventorylib.InventoryBuilder;
import lombok.Getter;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Initialize Kit Gui
 */

public class KitGui implements GuiInitializer {
    @Inject
    private MainModule mainModule;

    @Getter
    private InventoryBuilder inventoryBuilder;

    @Override
    public void initInv() {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                kitModule.getConfigManager().getKitYaml().getInt("inventory.rows"),
                kitModule.getConfigManager().getKitYaml().getString("inventory.title"),
                kitModule.getConfigManager().getKitYaml().getInt("inventory.page")).createInvs();
        mainModule.getInjector().getSingleton(NextItemGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(PreviousItemGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(ItemsGuiInit.class).initItems(inventoryBuilder);
        inventoryBuilder.build();
        this.inventoryBuilder = inventoryBuilder;
    }

    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getInventoryHash().get(1));
    }
}
