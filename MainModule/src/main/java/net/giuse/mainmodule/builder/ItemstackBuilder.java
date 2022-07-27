package net.giuse.mainmodule.builder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * ItemStack Builder
 */
public class ItemstackBuilder {
    private final Material material;
    private final int amount;
    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private String name;
    private List<String> lores;

    private short data;

    public ItemstackBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public ItemstackBuilder setLores(List<String> strings) {
        this.lores = strings;
        return this;
    }


    public ItemstackBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemstackBuilder setData(short data) {
        this.data = data;
        return this;
    }

    public ItemstackBuilder setEnchant(int lvl, Enchantment enchantment) {
        enchantments.put(enchantment, lvl);
        return this;
    }

    public ItemStack toItem() {
        ItemStack builtItemStack = new ItemStack(material, amount, data);
        ItemMeta buildItemStackMeta = builtItemStack.getItemMeta();
        ArrayList<String> convertedLore = new ArrayList<>();
        if (lores != null) {
            for (String lore : lores) {
                convertedLore.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            buildItemStackMeta.setLore(convertedLore);
        }
        if (name != null) {
            String newName = ChatColor.translateAlternateColorCodes('&', name);
            buildItemStackMeta.setDisplayName(newName);
        }
        if (!enchantments.isEmpty()) {
            for (Enchantment ench : enchantments.keySet()) {
                buildItemStackMeta.addEnchant(ench, enchantments.get(ench), true);
            }
        }
        builtItemStack.setItemMeta(buildItemStackMeta);
        return builtItemStack;
    }

}