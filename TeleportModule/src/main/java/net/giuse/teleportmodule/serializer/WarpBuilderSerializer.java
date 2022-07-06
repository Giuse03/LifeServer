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
        String[] splitWarp = str.split("-");
        String[] splitLocation = splitWarp[1].split(",");
        return new WarpBuilder(splitWarp[0],
                new Location(Bukkit.getWorld(splitLocation[0]),
                        Double.parseDouble(splitLocation[1]),
                        Double.parseDouble(splitLocation[2]),
                        Double.parseDouble(splitLocation[3]),
                        Float.parseFloat(splitLocation[4]),
                        Float.parseFloat(splitLocation[5])));
    }

}


