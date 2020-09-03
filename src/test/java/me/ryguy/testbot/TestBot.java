package me.ryguy.testbot;

import me.ryguy.discordapi.DiscordBot;

public class TestBot {
    private static DiscordBot bot;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You need to include a token in your arguments!");
            return;
        }
        bot = new DiscordBot(args[0], "!");
        bot.loginBot();

        new TestCommand().register();
        new TestListener().register();
        new TestWorkFlow().register();

        bot.endStartup();
    }
}