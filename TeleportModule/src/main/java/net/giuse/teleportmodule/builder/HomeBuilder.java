package net.giuse.teleportmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor
public class HomeBuilder {

    private final HashMap<String, Location> locations = new HashMap<>();


    private final String owner;

    @Override
    public String toString() {
        StringBuilder homeSerialized = new StringBuilder();
        int i = 0;
        homeSerialized.append(owner).append("_");
        for (String s : locations.keySet()) {
            i++;
            if (i == locations.keySet().size()) {
                homeSerialized.append(s)
                        .append(",")
                        .append(locations.get(s).getWorld().getName())
                        .append(",").append(locations.get(s).getX())
                        .append(",").append(locations.get(s).getY()
                        ).append(",").append(locations.get(s).getZ());
            } else {
                homeSerialized.append(s)
                        .append(",")
                        .append(locations.get(s).getWorld().getName())
                        .append(",").append(locations.get(s).getX())
                        .append(",").append(locations.get(s).getY()
                        ).append(",").append(locations.get(s).getZ()).append(";");
            }
        }
        return homeSerialized.toString();
    }

}
