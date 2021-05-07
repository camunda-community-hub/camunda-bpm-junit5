package org.camunda.springboottest.service;

import java.util.Date;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendDelegate implements JavaDelegate {
  
  private Greeter greeterService;
  
  @Autowired
  public SendDelegate(Greeter greeterService) {
    this.greeterService = greeterService;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String message = (String) execution.getVariable("message");
    String receiver = (String) execution.getVariable("name");
    
    Date greetingDate = greeterService.sendGreetings(receiver, message);
    
    execution.setVariable("sendDate", greetingDate);
  }

}
