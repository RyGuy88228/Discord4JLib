package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;
import me.ryguy.discordapi.DiscordBot;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class EventHandler {
    public EventHandler() {
        DiscordBot.getBot().getGateway().on(Event.class).subscribe(e -> {
            if (EventManager.getEvents().containsKey(e.getClass())) {
                for (Map.Entry<Listener, List<Method>> set : EventManager.getEvents().get(e.getClass()).entrySet()) {
                    for (Method m : set.getValue()) {
                        try {
                            m.invoke(set.getKey(), e);
                        } catch (Exception ex) {
                            DiscordBot.getBot().eventException.accept(ex, e);
                        }
                    }
                }
            }
        });
    }
}
