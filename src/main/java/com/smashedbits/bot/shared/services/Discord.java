package com.smashedbits.bot.shared.services;

import javax.security.auth.login.LoginException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@ApplicationScoped
public class Discord implements Messaging {
  private final JDA api;

  public Discord(@ConfigProperty(name = "discord.token") String token) throws LoginException, InterruptedException {
    this.api = JDABuilder
        .createLight(token)
        .setActivity(Activity.playing("Type /subscribe"))
        .build();
    api.awaitReady();
  }

  public void addEventListeners(Object... listener) {
    this.api.addEventListener(listener);
  }

  public void addCommand(CommandData... commands) {
    api.updateCommands().addCommands(commands).queue();
  }

  public void sendMessage(long userUID, String message) {
    api.retrieveUserById(userUID)
        .queue(
            user -> user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(message))
                .queue(),
            throwable -> {
              System.err.println(throwable);
            });
  }
}
