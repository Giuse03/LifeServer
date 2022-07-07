package net.giuse.teleportmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class HomeBuilder {
    private final HashMap<String, Location> locations = new HashMap<>();
    private final UUID owner;

    @Override
    public String toString() {
        StringBuilder homeSerialized = new StringBuilder();
        int i = 0;
        homeSerialized.append(owner.toString()).append(":");
        for (String s : locations.keySet()) {
            i++;
            homeSerialized.append(s).append(",").append(locations.get(s).getWorld().getName()).append(",").append(locations.get(s).getX())
                    .append(",").append(locations.get(s).getY()).append(",")
                    .append(locations.get(s).getZ()).append(",")
                    .append(locations.get(s).getYaw())
                    .append(",").append(locations.get(s).getPitch());
            if (i != locations.keySet().size()) {
                homeSerialized.append(",");
            }
        }
        return homeSerialized.toString();
    }

}
