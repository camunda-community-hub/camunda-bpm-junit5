/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.unittest.engine;

import static org.assertj.core.api.Assertions.*;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * @author Ingo Richtsmeier
 */
public class UseProcessEngineTest {
  
  private ProcessEngine usedProcessEngine = ProcessEngineConfiguration
      .createStandaloneInMemProcessEngineConfiguration()
      .setJdbcUrl("jdbc:h2:mem:camunda;DB_CLOSE_DELAY=1000")
      .buildProcessEngine();
  
  @RegisterExtension
  ProcessEngineExtension extension = ProcessEngineExtension
    .builder()
    .useProcessEngine(usedProcessEngine)
    .build();
  
  @BeforeEach
  public void setup() {
    init(usedProcessEngine);
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
