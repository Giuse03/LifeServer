package net.giuse.secretmessagemodule;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;


@Setter
@Getter
public class SecretChatBuilder {
    private Player sender;
    private Player receiver;
    private String text;
}
