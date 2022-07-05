package net.giuse.teleportmodule.gui;


import eu.giuse.inventorylib.InventoryBuilder;
import lombok.Getter;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class WarpGui implements GuiInitializer {

    @Inject
    private MainModule mainModule;
    @Getter
    private InventoryBuilder inventoryBuilder;

    @Override
    public void initInv() {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        InventoryBuilder inventoryBuilder = new InventoryBuilder(
                mainModule,
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.rows"),
                teleportModule.getFileManager().getWarpYaml().getString("inventory.title"),
                teleportModule.getFileManager().getWarpYaml().getInt("inventory.page")).createInvs();

        mainModule.getInjector().getSingleton(NextArrowGuiWarpInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(PreviousArrowWarpGuiInit.class).initItems(inventoryBuilder);
        mainModule.getInjector().getSingleton(ItemsGuiWarpInit.class).initItems(inventoryBuilder);
        inventoryBuilder.build();

        this.inventoryBuilder = inventoryBuilder;
    }

    public void openInv(Player player) {
        player.openInventory(inventoryBuilder.getInventoryHash().get(1));
    }
}