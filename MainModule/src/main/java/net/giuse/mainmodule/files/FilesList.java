package net.giuse.mainmodule.files;

public enum FilesList {
    DEFAULT_CONFIG("config.yml"),
    KIT_GUI("kit_gui_config.yml"),
    WARP_GUI("warp_gui_config.yml"),
    MESSAGE_SIMPLE_COMMAND("messages/messages_simple_command.yml"),
    MESSAGE_WARP("messages/messages_warp.yml"),
    MESSAGE_SPAWN("messages/messages_spawn.yml"),
    MESSAGE_HOME("messages/messages_home.yml"),
    MESSAGE_ECONOMY("messages/messages_economy.yml"),
    MESSAGE_TELEPORT("messages/messages_teleport.yml"),
    MESSAGE_SECRET_CHAT("messages/messages_secret_chat.yml"),
    MESSAGE_KIT("messages/messages_kit.yml");

    private final String path;

    FilesList(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
