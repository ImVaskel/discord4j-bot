package xyz.vaskel.discord4JBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vaskel.discord4JBot.commands.TestCommand;
import xyz.vaskel.discord4JBot.utils.Command;
import xyz.vaskel.discord4JBot.utils.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Bot {

    private HashMap<String, Command> commandHashMap;
    private GatewayDiscordClient client;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Config config;

    public Bot() throws IOException {

        config = new ObjectMapper().readValue(new File("config.json"), Config.class);

        commandHashMap = new HashMap<>();

        commandHashMap.put("test", new TestCommand());
    }

    public static void main(String[] args) throws IOException {
        new Bot().run();
    }

    private void initializeClient(){
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    logger.info(String.format(
                            "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
                    ));
                });

        client.on(MessageCreateEvent.class).subscribe(event -> {
            String content = event.getMessage().getContent();
            if (!content.startsWith(config.getPrefix())){
                return;
            }

            Command cmd = commandHashMap.get(content.split(" ")[0].substring(1));

            if (cmd == null){
                return;
            }

            cmd.execute(event);

        });

    }

    private void run() throws FileNotFoundException {

        client = DiscordClientBuilder.create(config.getToken())
                .build()
                .login()
                .block();

        initializeClient();

        client.onDisconnect().block();
    }

}
