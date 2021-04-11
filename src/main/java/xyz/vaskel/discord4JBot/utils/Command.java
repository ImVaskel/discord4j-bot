package xyz.vaskel.discord4JBot.utils;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface Command {

    public void execute(MessageCreateEvent event);

}
