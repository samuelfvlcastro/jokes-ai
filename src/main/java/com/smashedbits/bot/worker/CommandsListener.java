package com.smashedbits.bot.worker;

import com.smashedbits.bot.shared.model.Subscriber;
import com.smashedbits.bot.shared.services.Discord;
import com.smashedbits.bot.shared.services.Subscribers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@ApplicationScoped
public class CommandsListener extends ListenerAdapter {
  private final Discord provider;
  private final Subscribers service;

  @Inject
  public CommandsListener(Discord provider, Subscribers service) {
    this.provider = provider;
    this.service = service;
  }

  void onStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
    this.provider.addEventListeners(this);
    addCommands();
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "subscribe":
        User user = event.getUser();
        if (!insertSubscriber(user, event.getOption("category").getAsString())) {
          event.reply("Could not subscribe you, no jokes for you!").queue();
          return;
        }
        event.reply("Thank you for subscribing, check your your DM's for today's Joke!").queue();
        return;
      default:
        event.reply(String.format("command '%s' not implemented", event.getName())).queue();
        return;
    }
  }

  private void addCommands() {
    provider.addCommand(
        Commands.slash("subscribe", "Subscribe to daily jokes")
            .addOption(OptionType.STRING, "category", "The category of joke you want", true));
  }

  private boolean insertSubscriber(User user, String category) {
    Subscriber subscriber = new Subscriber();
    subscriber.setId(user.getIdLong());
    subscriber.setName(user.getName());
    subscriber.setJokeCategory(category);

    return service.upsertSubscriber(subscriber);
  }
}
