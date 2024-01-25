package com.smashedbits.bot.api.controllers;

import java.util.List;

import com.smashedbits.bot.shared.model.Subscriber;
import com.smashedbits.bot.shared.services.Subscribers;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/subs")
public class SubscribersController {
  private final Subscribers service;

  @Inject
  public SubscribersController(Subscribers service) {
    this.service = service;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response handleGetSubscribers() {
    List<Subscriber> subscribers = service.fetchSubscribers();
    return Response.ok(subscribers).build();
  }

  @DELETE
  @Path("/{id}")
  public Response handleDeleteSubscriber(@PathParam("id") long id) {
    if (service.deleteSubscriberById(id)) {
      return Response.ok().build();
    }
    return Response.status(Status.BAD_REQUEST).build();
  }
}
