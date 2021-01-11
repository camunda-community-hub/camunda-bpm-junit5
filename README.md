# camunda-bpm-junit5

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.camunda.bpm.extension/camunda-bpm-junit5/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.camunda.bpm.extension/camunda-bpm-junit5)

Camunda Community Extension to write process tests with JUnit5

## Usage

### Maven dependency
Add the dependency to your pom.xml

    <dependency>
      <groupId>org.camunda.bpm.extension</groupId>
      <artifactId>camunda-bpm-junit5</artifactId>
      <version>1.0.0</version>
      <scope>test</scope>
    </dependency>

### Test code
Add the annotation to your test class:

    @ExtendWith(ProcessEngineExtension.class)
    
For further access provide a field where the process engine gets injected:

    public ProcessEngine processEngine; 
    
Or register the extension from the builder:

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
      .configurationResource("audithistory.camunda.cfg.xml")
      .build();
    
and access the process engine from the extension object:

    RuntimeService runtimeService = extension.getProcessEngine().getRuntimeService(); 


### Examples
To use the JUnit 5 extension together with camunda-bpm-assert have a look at the [example-camunda-bpm-assert-junit5](examples/camunda-bpm-assert/README.md).



    
