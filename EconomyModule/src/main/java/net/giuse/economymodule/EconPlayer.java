package net.giuse.economymodule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class EconPlayer {
    private final String player;
    @Setter
    private double balance;
}
