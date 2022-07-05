package net.giuse.teleportmodule.teleporrequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.teleportmodule.enums.TpType;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public class PendingRequest {
    private final Player sender;
    private final Player receiver;
    private final TpType tpType;

}
