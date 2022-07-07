package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnBuilderSerializer implements Serializer<SpawnBuilder> {
    /*
     * Convert String to SpawnBuilder
     */
    @Override
    public String encode(SpawnBuilder spawnBuilder) {
        return spawnBuilder.toString();
    }

    /*
     * Convert SpawnBuilder to String
     */
    @Override
    public SpawnBuilder decoder(String str) {
        //Split String
        String[] spawn = str.split(",");

        //Insert args for build a SpawnBuilder
        return new SpawnBuilder(new Location(
                Bukkit.getWorld(spawn[0]),
                Double.parseDouble(spawn[1]),
                Double.parseDouble(spawn[2]),
                Double.parseDouble(spawn[3]),
                Float.parseFloat(spawn[4]),
                Float.parseFloat(spawn[5])));
    }

}


