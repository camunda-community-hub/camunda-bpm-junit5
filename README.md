# camunda-bpm-junit5

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.camunda.bpm.extension/camunda-bpm-junit5/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.camunda.bpm.extension/camunda-bpm-junit5) [![](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)

Camunda Community Extension to write process tests with JUnit5

## Usage

### Maven dependency
Add the dependency to your pom.xml

    <dependency>
      <groupId>org.camunda.bpm.extension</groupId>
      <artifactId>camunda-bpm-junit5</artifactId>
      <version>1.0.2</version>
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

If you don't want to create a configuration file, you can add a process engine, that you configure programmatically:

    public ProcessEngine myProcessEngine = ProcessEngineConfiguration
        .createStandaloneInMemProcessEngineConfiguration()
        .setJdbcUrl("jdbc:h2:mem:camunda;DB_CLOSE_DELAY=1000")
        .buildProcessEngine();
    
    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension
        .builder()
        .useProcessEngine(myProcessEngine)
        .build();
    
### Examples
To use the JUnit 5 extension together with camunda-bpm-assert have a look at the [example-camunda-bpm-assert-junit5](examples/camunda-bpm-assert/README.md).

The example about [example-engine-camunda-bpm-assert-junit5](examples/engine-camunda-bpm-assert/README.md) shows how to create and register a process engine programmatically.

    
