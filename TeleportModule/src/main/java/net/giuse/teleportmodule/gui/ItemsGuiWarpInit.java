package net.giuse.teleportmodule.gui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import inventorylib.ButtonBuilder;
import inventorylib.InventoryBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.gui.ItemInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ItemsGuiWarpInit implements ItemInitializer {
    @Inject
    private MainModule mainModule;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        ConfigurationSection configurationSection = teleportModule.getFileManager().getWarpYaml().getConfigurationSection("inventory.items");
        configurationSection.getKeys(false).forEach(string -> {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (!string.equalsIgnoreCase("previouspage") && !string.equalsIgnoreCase("nextpage")) {

                //Create ItemStackBuilder
                ItemstackBuilder itemstackBuilder = new ItemstackBuilder(XMaterial.matchXMaterial(itemsConfig.getString("material").toUpperCase()).get().parseMaterial(), itemsConfig.getInt("amount"));
                itemstackBuilder.setData((short) itemsConfig.getInt("data"));
                itemstackBuilder.setName(itemsConfig.getString("display-name"));

                //Check there are enchantments from section
                if (itemsConfig.getString("enchant") != null) itemstackBuilder
                        .setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                                XEnchantment.matchXEnchantment(itemsConfig.getString("enchant").split(":")[0]).get().getEnchant());

                //Check there are lores from section
                if (!itemsConfig.getStringList("lore").isEmpty()) {
                    itemstackBuilder.setLores(itemsConfig.getStringList("lore"));
                }

                //Create a button
                ButtonBuilder button = new ButtonBuilder(
                        inventoryBuilder,
                        itemsConfig.getInt("position"),
                        itemsConfig.getInt("page"),
                        itemstackBuilder.toItem(),
                        false, false, true);

                //Set Event of the button
                button.setEvent(inventoryClickEvent -> {
                    if (itemsConfig.getString("warp") != null) {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        player.performCommand("warp " + itemsConfig.getString("warp"));
                        inventoryClickEvent.getWhoClicked().closeInventory();
                    }
                });
                inventoryBuilder.addButton(button);
            }
        });
    }
}
