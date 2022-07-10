package net.giuse.economymodule.serializer;

import net.giuse.economymodule.EconPlayerSerialized;
import net.giuse.mainmodule.serializer.Serializer;

import java.util.UUID;

public class EconPlayerSerializer implements Serializer<EconPlayerSerialized> {

    @Override
    public String encode(final EconPlayerSerialized econPlayer) {
        return econPlayer.getUuid() + "," + econPlayer.getBalance();
    }


    @Override
    public EconPlayerSerialized decoder(final String str) {
        final String[] args = str.split(",");
        return new EconPlayerSerialized(UUID.fromString(args[0]), Double.parseDouble(args[1]));
    }
}
