package net.giuse.kitmodule.serializer;

import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.mainmodule.serializer.Serializer;

/**
 * KitSerializer serializer for save in database
 */

public class KitSerializer implements Serializer<KitBuilder> {

    /**
     * Convert String to KitBuilder
     */
    @Override
    public KitBuilder decoder(String kitString) {
        //Split Kit Serialized
        String[] valueKitBuilder = kitString.split(":");

        KitBuilder kitBuilder = new KitBuilder(valueKitBuilder[0],Integer.parseInt(valueKitBuilder[1]));

        for (String s : valueKitBuilder[2].split(",")) {
            kitBuilder.getItems().add(s);
        }
        return kitBuilder;
    }

    /**
     * Convert String to KitBuilder
     */
    @Override
    public String encode(KitBuilder kitBuilder) {
        StringBuilder sb = new StringBuilder();

        //Insert name in a StringBuilder
        sb.append(kitBuilder.getName()).append(":");

        //Insert cooldown of Kit in a StringBuilder
        sb.append(kitBuilder.getCoolDown()).append(":");

        //Insert serialized itemstack in a StringBuilder
        int size = kitBuilder.getItems().size();
        for (int i = 0; i < kitBuilder.getItems().size(); i++) {
            if(i == (size - 1)){
                sb.append(kitBuilder.getItems().get(i));
            }else{
                sb.append(kitBuilder.getItems().get(i)).append(",");
            }
        }

        //return KitBuilder Serialized
        return sb.toString();
    }

}


