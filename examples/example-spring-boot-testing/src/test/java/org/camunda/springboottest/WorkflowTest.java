package org.camunda.springboottest;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.springboottest.service.Greeter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(args = {
    "--spring.datasource.url=jdbc:h2:mem:camunda;DB_CLOSE_ON_EXIT=FALSE",  
    "--camunda.bpm.job-execution.enabled=false"
    })
public class WorkflowTest {
  
  @MockBean
  public Greeter mockedGreeter;

  @Autowired
  public RuntimeService runtimeService;
  
  Date testDate = new Date();
  
  @BeforeEach
  void initMocks() {
    Mockito.when(
        mockedGreeter.sendGreetings(anyString(), anyString()))
    .thenReturn(testDate);    
  }
  
  @Test
  public void completeProcess() {
    ProcessInstance processInstance = runtimeService
        .startProcessInstanceByKey("testing-process", withVariables("name", "Ingo"));
    
    assertThat(processInstance).isWaitingAt(findId("Say hello to\ndemo"));
    
    complete(task(findId("Say hello to\ndemo")), withVariables("known", true));
    
    assertThat(processInstance).isEnded()
        .variables().contains(entry("sendDate", testDate));
    verify(mockedGreeter).sendGreetings("Ingo", "hello Ingo, nice to meet you again!");
  }

  @Test
  public void testUnknownPerson() {
    ProcessInstance processInstance = runtimeService
        .createProcessInstanceByKey("testing-process")
        .startAfterActivity(findId("Say hello to\ndemo"))
        .setVariables(withVariables("name", "Mr Y.", "known", false))
        .execute();
    
    assertThat(processInstance).isWaitingAt(findId("Send invitation"));
    execute(job());
    assertThat(processInstance).isEnded().hasPassed(findId("Invitation sent"))
        .variables().contains(entry("sendDate", testDate));
    verify(mockedGreeter).sendGreetings("Mr Y.", "Hello Mr Y., I would like to meet you");
  }
}
