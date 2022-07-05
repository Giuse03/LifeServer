package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.builder.SpawnBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnBuilderSerializer implements Serializer<SpawnBuilder> {

    @Override
    public String encode(SpawnBuilder spawnBuilder) {
        return spawnBuilder.toString();
    }

    @Override
    public SpawnBuilder decoder(String str) {
        String[] spawn = str.split(",");

        return new SpawnBuilder(new Location(
                Bukkit.getWorld(spawn[0]),
                Double.parseDouble(spawn[1]),
                Double.parseDouble(spawn[2]),
                Double.parseDouble(spawn[3]),
                Float.parseFloat(spawn[4]),
                Float.parseFloat(spawn[5])));
    }

}


