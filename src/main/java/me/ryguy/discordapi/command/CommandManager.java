package me.ryguy.discordapi.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandManager {
    protected static List<Command> commands = new ArrayList<>();

    public static void registerCommand(Command cmd) {
        commands.add(cmd);
    }

    public static void unregisterCommand(Command cmd) {
        if (commands.contains(cmd))
            commands.remove(cmd);
        else
            System.out.println("Command '" + cmd.getName() + "' is not registered, can't unregister it!");
    }

    public static Command getCommand(String s) {
        Optional<Command> ret = commands.parallelStream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(s) || cmd.getAliases().contains(s))
                .findFirst();

        if (ret.isPresent())
            return ret.get();

        return null;
    }

    public static List<Command> getRegisteredCommands() {
        return commands;
    }
}
