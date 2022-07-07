package net.giuse.teleportmodule.gui;


import eu.giuse.inventorylib.InventoryBuilder;
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
    private final MainModule mainModule;
    private final TeleportModule teleportModule;
    @Getter
    private InventoryBuilder inventoryBuilder;

    @Inject
    public WarpGui(MainModule mainModule) {
        this.mainModule = mainModule;
        teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
    }

    /*
     * Initialize Inventory
     */
    @Override
    public void initInv() {

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