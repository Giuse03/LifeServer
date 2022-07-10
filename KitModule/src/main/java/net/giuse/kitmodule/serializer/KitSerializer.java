package net.giuse.kitmodule.serializer;

import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.serializer.serializedobject.KitSerialized;
import net.giuse.mainmodule.serializer.Serializer;

/**
 * KitSerializer serializer for save in database
 */

public class KitSerializer implements Serializer<KitSerialized> {


    /**
     * Convert String to KitBuilder
     */
    @Override
    public KitSerialized decoder(String kitString) {
        //Split Kit Serialized
        String[] valueKitBuilder = kitString.split(":");
        return new KitSerialized(valueKitBuilder[0], new KitBuilder(Integer.parseInt(valueKitBuilder[1])).setBase(valueKitBuilder[2]));
    }

    /**
     * Convert String to KitBuilder
     */
    @Override
    public String encode(KitSerialized kitSerialized) {
        //Insert name in a StringBuilder
        return kitSerialized.getName() + ":" +
                //Insert cooldown of Kit in a StringBuilder
                kitSerialized.getKitBuilder().getCoolDown() + ":" +
                //Insert serialized itemstack in a StringBuilder
                kitSerialized.getKitBuilder().getBase64();
    }


}


