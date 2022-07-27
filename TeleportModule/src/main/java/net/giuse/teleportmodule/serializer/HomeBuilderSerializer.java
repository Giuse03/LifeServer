package net.giuse.teleportmodule.serializer;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.UUID;

public class HomeBuilderSerializer implements Serializer<HomeSerialized> {

    @Inject
    private MainModule mainModule;

    /*
     * Convert String to HomeBuilder
     */
    @Override
    public String encode(HomeSerialized homeSerializedObject) {
        StringBuilder homeSerialized = new StringBuilder();
        int i = 0;
        homeSerialized.append(homeSerializedObject.getOwner().toString()).append(":");
        for (String s : homeSerializedObject.getLocations().keySet()) {
            i++;
            homeSerialized.append(s).append(",").append(homeSerializedObject.getLocations().get(s).getWorld().getName())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getX())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getY()).append(",")
                    .append(homeSerializedObject.getLocations().get(s).getZ()).append(",")
                    .append(homeSerializedObject.getLocations().get(s).getYaw())
                    .append(",").append(homeSerializedObject.getLocations().get(s).getPitch());
            if (i != homeSerializedObject.getLocations().size()) {
                homeSerialized.append(";");
            }
        }
        return homeSerialized.toString();
    }

    /*
     * Convert HomeBuilder to String
     */
    @Override
    public HomeSerialized decoder(String str) {
        //Split String
        String[] homes = str.split(":");

        //Insert args for builder a HomeSerialized and check if player has home
        HomeSerialized homeSerialized = new HomeSerialized(UUID.fromString(homes[0]), new HashMap<>());
        if (homes.length == 1) return homeSerialized;

        //Deserialize home and insert it in HomeBuilder
        if (homes[1].contains(";")) {
            for (String s : homes[1].split(";")) {
                String[] variousHome = s.split(",");
                homeSerialized.getLocations().put(variousHome[0], new Location(Bukkit.getWorld(variousHome[1]), Double.parseDouble(variousHome[2]), Double.parseDouble(variousHome[3]), Double.parseDouble(variousHome[4])));
            }
        } else {
            String[] defaultHome = homes[1].split(",");
            homeSerialized.getLocations().put(defaultHome[0], new Location(Bukkit.getWorld(defaultHome[1]), Double.parseDouble(defaultHome[2]), Double.parseDouble(defaultHome[3]), Double.parseDouble(defaultHome[4])));

        }
        return homeSerialized;
    }

}


