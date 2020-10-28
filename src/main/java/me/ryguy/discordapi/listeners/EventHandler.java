package me.ryguy.discordapi.listeners;

import discord4j.core.event.domain.Event;
import me.ryguy.discordapi.DiscordBot;
import me.ryguy.discordapi.util.MultiThreader;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class EventHandler implements MultiThreader {
    public void init() {
        DiscordBot.getBot().getGateway().on(Event.class).subscribe(e -> {
            thread("EventHandler", () -> {
                if (EventHolder.of(e.getClass()) != null) {
                    EventHolder holder = EventHolder.of(e.getClass());
                    DiscordBot.getBot().checkEventCancellation.accept(e);
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
        });
    }
}
