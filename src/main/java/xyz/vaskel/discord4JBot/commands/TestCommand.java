package xyz.vaskel.discord4JBot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import xyz.vaskel.discord4JBot.utils.Command;

public class TestCommand implements Command {
    @Override
    public void execute(MessageCreateEvent event) {
        Message msg = event.getMessage();

        msg.getChannel().flatMap(channel -> channel.createMessage("Hello, World!"))
        .subscribe();
    }
}
