package com.smashedbits.bot.worker;

import java.util.List;

import com.smashedbits.bot.shared.model.Subscriber;
import com.smashedbits.bot.shared.services.Messaging;
import com.smashedbits.bot.shared.services.Subscribers;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JokesSender {
  private final Messaging provider;
  private final Subscribers service;

  @Inject
  public JokesSender(Messaging provider, Subscribers service) {
    this.provider = provider;
    this.service = service;
  }

  @Scheduled(every = "20s")
  void Send(ScheduledExecution execution) {
    List<Subscriber> subscribers = service.fetchSubscribers();
    subscribers.forEach(subscriber -> {
      provider.sendMessage(subscriber.getId(), "Have a new joke dude!");
    });
  }
}
