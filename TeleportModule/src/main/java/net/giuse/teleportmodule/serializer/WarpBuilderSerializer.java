package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.builder.WarpBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WarpBuilderSerializer implements Serializer<WarpBuilder> {

    @Override
    public String encode(WarpBuilder warpBuilder) {
        return warpBuilder.toString();
    }

    @Override
    public WarpBuilder decoder(String str) {
        String[] splitWarp = str.split(",");
        return new WarpBuilder(splitWarp[0],
                new Location(Bukkit.getWorld(splitWarp[1]),
                        Double.parseDouble(splitWarp[2]),
                        Double.parseDouble(splitWarp[3]),
                        Double.parseDouble(splitWarp[4]),
                        Float.parseFloat(splitWarp[5]),
                        Float.parseFloat(splitWarp[6])));
    }

}


