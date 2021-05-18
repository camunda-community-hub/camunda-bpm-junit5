package org.camunda.bpm.unittest;

import static org.assertj.core.api.Assertions.*;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class UseProcessEngineTest {
  
  @RegisterExtension
  static ProcessEngineExtension extension = ProcessEngineExtension
    .builder()
    .useProcessEngine(ProcessEngineConfiguration
        .createStandaloneInMemProcessEngineConfiguration()
        // Use a new database to resolve the conflict with existing 
        // in-memory-database. The tables will be removed after the test.
        .setJdbcUrl("jdbc:h2:mem:camunda-test")
        .buildProcessEngine())
    .build();
  
  @BeforeAll
  public static void setup() {
    init(extension.getProcessEngine());
  }
  
  @AfterAll
  public static void closeProcessEngine() {
    processEngine().close();
  }
  
  @Test
  @Deployment(resources = "testProcess.bpmn")
  public void testHappyPath() {
    // Given we create a new process instance
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess");
    // Then it should be active
    assertThat(processInstance).isActive();
    // And it should be the only instance
    assertThat(processInstanceQuery().count()).isEqualTo(1);
    // And there should exist just a single task within that process instance
    assertThat(task(processInstance)).isNotNull();

    // When we complete that task
    complete(task(processInstance));
    // Then the process instance should be ended
    assertThat(processInstance).isEnded();
  }

}
