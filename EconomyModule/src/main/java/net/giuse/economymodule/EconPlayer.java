package net.giuse.economymodule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class EconPlayer {
    private final UUID player;
    @Setter
    private double balance;
}
