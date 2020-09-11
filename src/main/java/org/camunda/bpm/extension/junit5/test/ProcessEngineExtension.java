package org.camunda.bpm.extension.junit5.test;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import org.camunda.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
import org.camunda.bpm.engine.impl.test.TestHelper;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEngineExtension implements TestWatcher, TestInstancePostProcessor, BeforeTestExecutionCallback, ParameterResolver {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineExtension.class);
  protected ProcessEngine processEngine;
  protected ProcessEngineConfigurationImpl processEngineConfiguration;
  
  protected String configurationResource = "camunda.cfg.xml";
  protected String configurationResourceCompat = "activiti.cfg.xml";

  public final static List<String> RESOURCE_SUFFIXES = new ArrayList<>();

  static {
    RESOURCE_SUFFIXES.addAll(Arrays.asList(BpmnDeployer.BPMN_RESOURCE_SUFFIXES));
    RESOURCE_SUFFIXES.addAll(Arrays.asList(CmmnDeployer.CMMN_RESOURCE_SUFFIXES));
    RESOURCE_SUFFIXES.addAll(Arrays.asList(DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES));
  }

  public static ProcessEngineExtension builder() {
    return new ProcessEngineExtension();
  }

  public ProcessEngineExtension configurationResource(String configurationResource) {
    this.setConfigurationResource(configurationResource);
    return this;
  }
  
  public ProcessEngine getProcessEngine() {
    return processEngine;
  }

  public void setProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }
  
  public String getConfigurationResource() {
    return configurationResource;
  }

  public void setConfigurationResource(String configurationResource) {
    this.configurationResource = configurationResource;
  }

  public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
    return processEngineConfiguration;
  }

  public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
    this.processEngineConfiguration = processEngineConfiguration;
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    if (processEngine == null) {
      initializeProcessEngine();
    }
    Arrays.stream(testInstance.getClass().getDeclaredFields())
      .filter(field -> field.getType() == ProcessEngine.class)
      .forEach(field -> inject(testInstance, field));
  }

  protected void initializeProcessEngine() {
    try {
      processEngine = TestHelper.getProcessEngine(configurationResource);
    } catch (RuntimeException ex) {
      if (ex.getCause() != null && ex.getCause() instanceof FileNotFoundException) {
        processEngine = TestHelper.getProcessEngine(configurationResourceCompat);
      } else {
        throw ex;
      }
    }
  }

  private void inject(Object instance, Field field) {
    field.setAccessible(true);
    try {
      field.set(instance, processEngine);
    } catch (IllegalAccessException iae) {
      throw new RuntimeException(iae);
    }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    LOG.debug("beforeTestExecution: {}", context.getDisplayName());
    
    Method testMethod = context.getTestMethod().get();
    Class<?> testClass = context.getTestClass().get();
    
    DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
        .createDeployment()
        .name(testClass.getSimpleName()+"."+testMethod.getName());
    
    Deployment methodAnnotation = testMethod.getAnnotation(Deployment.class);
    if (methodAnnotation != null) {
      LOG.info("annotation @Deployment creates deployment for {}.{}", testClass.getName(), testMethod.getName());
      deploymentBuilder.addClasspathResource(
          TestHelper.getBpmnProcessDefinitionResource(testClass, testMethod.getName()));
    } else {
      Deployment classAnnotation = testClass.getAnnotation(Deployment.class);
      deploymentBuilder.addClasspathResource(
          TestHelper.getBpmnProcessDefinitionResource(testClass, null));
    }
    deploymentBuilder.deploy();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    if (parameterContext.getParameter().getType().equals(ProcessEngine.class)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    if (ProcessEngine.class.equals(parameterContext.getParameter().getType())) {
      LOG.debug("resolve the processEngine as parameter");
      return getProcessEngine();
    } else {
      return null;
    }
  }
  
  public ProcessEngineExtension build() {
    if (processEngine == null) {
      initializeProcessEngine();
    }
    return this;
  }
  
}
