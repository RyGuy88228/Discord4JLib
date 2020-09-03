package me.ryguy.discordapi.command;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
public abstract class Command implements CommandExecutor {
    protected String name;
    protected List<String> aliases;

    protected List<Long> guildFilter;
    protected List<Long> userFilter;
    protected List<Long> channelFilter;
    protected List<Permission> permsFilter;
    protected boolean guildOnly;

    public Command(String name) {
        this.name = name;
        aliases = new ArrayList<>(); //Empty arraylist

        guildFilter = new ArrayList<>();
        userFilter = new ArrayList<>();
        channelFilter = new ArrayList<>();
        permsFilter = new ArrayList<>();
        guildOnly = false;
    }

    public Command(String name, String... aliases) {
        this.name = name;
        this.aliases = Arrays.asList(aliases);

        guildFilter = new ArrayList<>();
        userFilter = new ArrayList<>();
        channelFilter = new ArrayList<>();
        permsFilter = new ArrayList<>();
        guildOnly = false;
    }

    public void register() {
        CommandManager.registerCommand(this);
        System.out.println("Registered command " + this.getName() + "!");
    }

    public void unregister() {
        CommandManager.unregisterCommand(this);
        System.out.println("Unregistered command " + this.getName() + "!");
    }

    public boolean canExecute(Message msg) {
        if (!this.getChannelFilter().isEmpty())
            return this.getChannelFilter().contains(msg.getId().asLong());
        if (this.isGuildOnly())
            return msg.getGuildId().isPresent();
        if (!this.getGuildFilter().isEmpty())
            return msg.getGuildId().isPresent() && this.getGuildFilter().contains(msg.getGuildId());
        if (!this.getUserFilter().isEmpty())
            return msg.getAuthor().isPresent() && this.getUserFilter().contains(msg.getAuthor().get().getId().asLong());
        if (!this.permsFilter.isEmpty()) {
            Iterator<Permission> it = msg.getAuthorAsMember().block().getBasePermissions().block().iterator();
            while (it.hasNext()) {
                if (this.permsFilter.contains(it.next()))
                    return true;
            }
            return false;
        }
        return true;
    }
}
