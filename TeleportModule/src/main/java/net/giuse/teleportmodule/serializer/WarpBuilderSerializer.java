package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class WarpBuilderSerializer implements Serializer<WarpSerialized> {

    /*
     * Convert WarpBuilder to String
     */
    @Override
    public String encode(WarpSerialized warpSerialized) {
        return warpSerialized.getName().concat(":")
                .concat(warpSerialized.getLocation().getWorld().getName()).concat(",")
                .concat(String.valueOf(warpSerialized.getLocation().getX()))
                .concat(",").concat(String.valueOf(warpSerialized.getLocation().getY()))
                .concat(",").concat(String.valueOf(warpSerialized.getLocation().getZ()))
                .concat(",").concat(String.valueOf(warpSerialized.getLocation().getYaw()))
                .concat(",").concat(String.valueOf(warpSerialized.getLocation().getPitch()));
    }

    /*
     * Convert String to WarpBuilder
     */
    @Override
    public WarpSerialized decoder(String str) {
        //Split string
        String[] splitWarp = str.split(":");
        String[] splitLocation = splitWarp[1].split(",");

        //Insert args for build a WarpBuilder
        return new WarpSerialized(splitWarp[0],
                new Location(Bukkit.getWorld(splitLocation[0]),
                        Double.parseDouble(splitLocation[1]),
                        Double.parseDouble(splitLocation[2]),
                        Double.parseDouble(splitLocation[3]),
                        Float.parseFloat(splitLocation[4]),
                        Float.parseFloat(splitLocation[5])));
    }

}


