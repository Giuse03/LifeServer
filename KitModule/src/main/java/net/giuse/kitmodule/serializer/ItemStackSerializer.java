package net.giuse.kitmodule.serializer;

import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.serializer.Serializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class ItemStackSerializer implements Serializer<ItemStack> {

    @Override
    public ItemStack decoder(String str) {
        String[] valueItemStack = str.split(";");
        ItemstackBuilder itemStackBuild = new ItemstackBuilder(
                Material.getMaterial(valueItemStack[0].replace("material_", "")),
                Integer.parseInt(valueItemStack[2].replace("amount_", "")));
        itemStackBuild.setData(Short.parseShort(valueItemStack[1].replace("data_", "")));

        for (String strings : valueItemStack) {
            if (strings.contains("name_")) {
                itemStackBuild.setName(strings.replace("name_", ""));
            } else if (strings.contains("enchant_")) {
                String[] valueEnchant = strings.replace("enchant_", "").split(",");
                int i;
                for (i = 0; i < valueEnchant.length; i++) {
                    String[] valueEnchantAndLevel = valueEnchant[i].split(":");
                    int lvl = Integer.parseInt(valueEnchantAndLevel[1]);
                    itemStackBuild.setEnchant(lvl, Enchantment.getByName(valueEnchantAndLevel[0]));
                }
            } else if (strings.contains("lore_")) {
                itemStackBuild.setLores(strings.split(":"));
            }
        }
        return itemStackBuild.toItem();
    }


    @Override
    public String encode(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        sb.append("material_").append(itemStack.getType().toString()).append(";");
        sb.append("data_").append(itemStack.getDurability()).append(";");
        sb.append("amount_").append(itemStack.getAmount()).append(";");
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName())
            sb.append("name_").append(itemStack.getItemMeta().getDisplayName()).append(";");
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
            sb.append("enchant_");
            if (itemStack.getEnchantments() != null) {
                int i = 0;
                for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                    i++;
                    if ((i) == itemStack.getEnchantments().size()) {
                        sb.append(enchantment.getName()).append(":").append(itemStack.getEnchantmentLevel(enchantment));
                    } else {
                        sb.append(enchantment.getName()).append(":").append(itemStack.getEnchantmentLevel(enchantment)).append(",");
                    }
                }
                sb.append(";");
            }
        }
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            sb.append("lore_");
            int i = 0;
            for (String lores : itemStack.getItemMeta().getLore()) {
                i++;
                if (i == itemStack.getItemMeta().getLore().size()) {
                    sb.append(lores);
                } else sb.append(lores).append(":");
            }
            sb.append(";");
        }
        return sb.toString();
    }

}

