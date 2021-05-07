package org.camunda.springboottest.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Greeter {
  
  private static final Logger LOG = LoggerFactory.getLogger(Greeter.class);
  
  public Date sendGreetings(String receiver, String message) {
    LOG.info("Message sent to {}: {}", receiver, message);
    return new Date();
  }

}
