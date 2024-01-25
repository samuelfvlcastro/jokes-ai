package com.smashedbits.bot.api.controllers;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
  private final String apiToken;

  public AuthenticationFilter(@ConfigProperty(name = "api.token") String apiToken) {
    this.apiToken = apiToken;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      return;
    }

    String token = authHeader.substring(7);
    if (!token.equals(apiToken)) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      return;
    }
  }

}
