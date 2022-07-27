package ezmessage;


import ezmessage.messages.MessageActionbar;
import ezmessage.messages.MessageChat;
import ezmessage.messages.MessageTitle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MessageBuilder {
    private final MessageLoader messageLoader;
    private CommandSender commandSender;
    private String idMessage;

    /*
     * Set CommandSender to send Message
     */
    public MessageBuilder setCommandSender(CommandSender commandSender) {
        this.commandSender = commandSender;
        return this;
    }

    /*
     * Set ID of the messaage to search
     */
    public MessageBuilder setIDMessage(String ID) {
        this.idMessage = ID;
        return this;
    }

    /*
     * Send Message with replaces
     */
    @SneakyThrows
    public void sendMessage(TextReplacer... textReplacers) {
        for (String string : new String[]{"_chat", "_bossbar", "_title"}) {
            CompletableFuture.supplyAsync(() -> messageLoader.getCache().get(idMessage + string)).whenCompleteAsync(((message, throwable) -> {
                switch (message.getMessageType()) {
                    //SEND CHAT
                    case CHAT:
                        MessageChat messageChat = (MessageChat) message;
                        String messageToReplace = messageChat.getMessageChat();
                        if (textReplacers.length == 0) {
                            messageLoader.sendChat(commandSender, Component.text(messageToReplace));
                            break;
                        }
                        for (TextReplacer textReplacer : textReplacers) {
                            messageToReplace = textReplacer.setText(messageToReplace).returnReplace();
                        }
                        Component newComponentReplacedText = Component.text(messageToReplace);
                        messageLoader.sendChat(commandSender, newComponentReplacedText);
                        break;

                    //Send Action Bar
                    case ACTION_BAR:
                        if (commandSender instanceof Player) {
                            MessageActionbar messageActionbar = (MessageActionbar) message;
                            String placeHolder = messageActionbar.getMessageBar();
                            if (textReplacers.length == 0) {
                                messageLoader.sendActionBar((Player) commandSender, Component.text(placeHolder));
                                break;
                            }

                            for (TextReplacer textReplacer : textReplacers) {
                                placeHolder = textReplacer.setText(placeHolder).returnReplace();
                            }
                            Component newComponentReplaced = Component.text(placeHolder);
                            messageLoader.sendActionBar((Player) commandSender, newComponentReplaced);
                            break;
                        }
                        //Send Title
                    case TITLE:
                        if (commandSender instanceof Player) {
                            MessageTitle messageTitle = (MessageTitle) message;
                            String title = messageTitle.getTitle();
                            String subTitle = messageTitle.getSubTitle();
                            if (textReplacers.length == 0) {
                                TextComponent newTitleComponent = Component.text(title);
                                TextComponent newSubTitleComponent = Component.text(subTitle);
                                messageLoader.sendTitle((Player) commandSender, newTitleComponent, newSubTitleComponent, messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                                break;
                            }

                            for (TextReplacer textReplacer : textReplacers) {
                                title = textReplacer.setText(title).returnReplace();
                                subTitle = textReplacer.setText(subTitle).returnReplace();

                            }

                            TextComponent newTitleComponent = Component.text(title);
                            TextComponent newSubTitleComponent = Component.text(subTitle);
                            messageLoader.sendTitle((Player) commandSender, newTitleComponent, newSubTitleComponent, messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                        }
                }
            }));

        }
    }

}
