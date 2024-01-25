package com.smashedbits.bot.shared.services;

import java.util.List;

import com.smashedbits.bot.shared.model.Subscriber;
import com.smashedbits.bot.shared.repositories.SubscribersRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Subscribers {
  private final SubscribersRepository repository;

  @Inject
  public Subscribers(SubscribersRepository repository) {
    this.repository = repository;
  }

  public List<Subscriber> fetchSubscribers() {
    return repository.getSubscribers();
  }

  public boolean upsertSubscriber(Subscriber subscriber) {
    return repository.upsertSubscriber(subscriber);
  }

  public boolean deleteSubscriberById(long subscriberId) {
    return repository.deleteSubscriberById(subscriberId);
  }
}
