package me.ryguy.discordapi.util;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean bool);
}
