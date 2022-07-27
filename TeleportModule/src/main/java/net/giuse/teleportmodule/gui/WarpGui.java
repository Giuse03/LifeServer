package net.giuse.teleportmodule.gui;


import inventorylib.InventoryBuilder;
import lombok.Getter;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/*
 * Initialize Warp Gui
 */
public class WarpGui implements GuiInitializer {
    @Inject
    private MainModule mainModule;
    @Getter
    private InventoryBuilder inventoryBuilder;


    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);

        //Create Inventory Builder
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.rows"),
                teleportModule.getFileManager().getWarpYaml().getString("inventory.title"),
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.page")).createInvs();

        //Initialize items
        mainModule.getInjector().getSingleton(NextItemGuiWarpInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(PreviousInitWarpGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(ItemsGuiWarpInit.class).initItems(inventoryBuilder);

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