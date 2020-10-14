package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;
import me.ryguy.discordapi.DiscordBot;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class EventHandler {
    public EventHandler() {
        DiscordBot.getBot().getGateway().on(Event.class).subscribe(e -> {
            if (EventHolder.of(e.getClass()) != null) {
                EventHolder holder = EventHolder.of(e.getClass());
                for (Map.Entry<Listener, List<Method>> set : holder.getMethods().entrySet()) {
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
