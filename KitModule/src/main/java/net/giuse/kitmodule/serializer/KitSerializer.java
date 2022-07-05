package net.giuse.kitmodule.serializer;

import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.mainmodule.serializer.Serializer;
import org.bukkit.inventory.ItemStack;

public class KitSerializer implements Serializer<KitBuilder> {
    private final Serializer<ItemStack> itemStackSerializer = new ItemStackSerializer();

    @Override
    public KitBuilder decoder(String kitString) {
        String[] valueKitBuilder = kitString.split(";");
        KitBuilder kitBuilder = new KitBuilder();
        kitBuilder.setName(valueKitBuilder[0]);
        kitBuilder.setCoolDown(Integer.parseInt(valueKitBuilder[1]));
        for (String s : valueKitBuilder[2].split(",")) {
            kitBuilder.getItems().add(itemStackSerializer.decoder(s));
        }
        return kitBuilder;
    }

    @Override
    public String encode(KitBuilder kitBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append(kitBuilder.getName()).append(";");
        sb.append(kitBuilder.getCoolDown()).append(";");
        int i = 0;
        for (ItemStack itemStack : kitBuilder.getItems()) {
            i++;
            if ((i) == kitBuilder.getItems().size()) {
                sb.append(itemStackSerializer.encode(itemStack));
            } else {
                sb.append(itemStackSerializer.encode(itemStack)).append(",");
            }
        }
        return sb.toString();
    }

}


