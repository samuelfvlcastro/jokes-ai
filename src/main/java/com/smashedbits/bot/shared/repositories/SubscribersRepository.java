package com.smashedbits.bot.shared.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.smashedbits.bot.shared.model.Subscriber;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SubscribersRepository {
  @Inject
  DataSource dataSource;

  private String selectAllSQL = "SELECT id, name, joke_category FROM subscribers";
  private String selectSQL = "SELECT id, name, joke_category FROM subscribers WHERE id = ?";
  private String insertSQL = "INSERT INTO subscribers (id, name, joke_category) VALUES (?, ?, ?)" +
      " ON CONFLICT(id) DO UPDATE SET" +
      " name = excluded.name," +
      " joke_category = excluded.joke_category";
  private String deleteById = "DELETE FROM subscribers WHERE id = ?";

  public List<Subscriber> getSubscribers() {
    List<Subscriber> subscribers = new ArrayList<Subscriber>();
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statment = connection.prepareStatement(selectAllSQL);
      ResultSet resultSet = statment.executeQuery();

      while (resultSet.next()) {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(resultSet.getLong("id"));
        subscriber.setName(resultSet.getString("name"));
        subscriber.setJokeCategory(resultSet.getString("joke_category"));
        subscribers.add(subscriber);
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
    return subscribers;
  }

  public Subscriber getSubscriberById(int id) {
    Subscriber subscriber = null;
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statment = connection.prepareStatement(selectSQL);
      statment.setInt(1, id);
      ResultSet resultSet = statment.executeQuery();

      if (resultSet.next()) {
        subscriber = new Subscriber();
        subscriber.setId(resultSet.getLong("id"));
        subscriber.setName(resultSet.getString("name"));
        subscriber.setJokeCategory(resultSet.getString("joke_category"));
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
    return subscriber;
  }

  public boolean upsertSubscriber(Subscriber subscriber) {
    boolean isInserted = false;
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(insertSQL);
      statement.setLong(1, subscriber.getId());
      statement.setString(2, subscriber.getName());
      statement.setString(3, subscriber.getJokeCategory());

      int rowsAffected = statement.executeUpdate();
      isInserted = rowsAffected > 0;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
    return isInserted;
  }

  public boolean deleteSubscriberById(long id) {
    boolean isDeleted = false;
    try (Connection connection = dataSource.getConnection()) {
      System.out.println(id);
      System.out.println(deleteById);
      PreparedStatement statement = connection.prepareStatement(deleteById);
      statement.setLong(1, id);

      int rowsAffected = statement.executeUpdate();
      isDeleted = rowsAffected > 0;
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    }
    return isDeleted;
  }
}
