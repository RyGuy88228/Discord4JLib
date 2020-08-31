package me.ryguy.testbot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import me.ryguy.discordapi.listeners.DiscordEvent;
import me.ryguy.discordapi.listeners.Listener;

public class TestListener implements Listener {
    @DiscordEvent
    public void onMessage(MessageCreateEvent e) {
        System.out.println("Recieved Message from Event: " + e.getMessage());
    }

    @DiscordEvent
    public void onMessageUpdate(MessageUpdateEvent e) {
        System.out.println("Message edited in channel " + e.getChannelId());
    }
}
