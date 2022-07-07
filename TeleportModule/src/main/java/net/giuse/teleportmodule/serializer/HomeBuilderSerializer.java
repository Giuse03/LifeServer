package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.builder.HomeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class HomeBuilderSerializer implements Serializer<HomeBuilder> {

    /*
     * Convert String to HomeBuilder
     */
    @Override
    public String encode(HomeBuilder homeBuilder) {
        return homeBuilder.toString();
    }

    /*
     * Convert HomeBuilder to String
     */
    @Override
    public HomeBuilder decoder(String str) {
        //Split String
        String[] homes = str.split(":");

        //Insert args for builder a HomeBuilder
        HomeBuilder homeBuilder = new HomeBuilder(UUID.fromString(homes[0]));

        //Check if player has home
        if (homes.length == 1) return homeBuilder;

        //Deserialize home and insert it in HomeBuilder
        if (homes[1].contains(";")) {
            for (String s : homes[1].split(";")) {
                String[] variousHome = s.split(",");
                homeBuilder.getLocations().put(variousHome[0], new Location(Bukkit.getWorld(variousHome[1]), Double.parseDouble(variousHome[2]), Double.parseDouble(variousHome[3]), Double.parseDouble(variousHome[4])));
            }
        } else {
            String[] defaultHome = homes[1].split(",");
            homeBuilder.getLocations().put(defaultHome[0], new Location(Bukkit.getWorld(defaultHome[1]), Double.parseDouble(defaultHome[2]), Double.parseDouble(defaultHome[3]), Double.parseDouble(defaultHome[4])));

        }
        return homeBuilder;
    }

}


