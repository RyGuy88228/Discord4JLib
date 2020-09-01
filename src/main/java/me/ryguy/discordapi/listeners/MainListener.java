package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import me.ryguy.discordapi.util.WorkFlow;

public class MainListener implements Listener {
    @DiscordEvent
    public void onMessageCreate(MessageCreateEvent e) {
        WorkFlow.handle(e);
    }
}
