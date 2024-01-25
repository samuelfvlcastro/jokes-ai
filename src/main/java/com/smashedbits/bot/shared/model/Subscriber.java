package com.smashedbits.bot.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscriber {

  @JsonProperty("id")
  private long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("joke_category")
  private String jokeCategory;

  public Subscriber() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getJokeCategory() {
    return jokeCategory;
  }

  public void setJokeCategory(String jokeCategory) {
    this.jokeCategory = jokeCategory;
  }
}
