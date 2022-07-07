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
        return new KitBuilder(valueKitBuilder[0], Integer.parseInt(valueKitBuilder[1])).setBase(valueKitBuilder[2]);
    }

    /**
     * Convert String to KitBuilder
     */
    @Override
    public String encode(KitBuilder kitBuilder) {
        //Insert name in a StringBuilder
        return kitBuilder.getName() + ":" +
                //Insert cooldown of Kit in a StringBuilder
                kitBuilder.getCoolDown() + ":" +
                //Insert serialized itemstack in a StringBuilder
                kitBuilder.getBase64();
    }

}


