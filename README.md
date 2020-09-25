# camunda-bpm-junit5
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
    
and provide a field where the process engine gets injected for further access:

    public ProcessEngine processEngine; 
    
Or register the extension from the builder:

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder()
      .configurationResource("audithistory.camunda.cfg.xml")
      .build();
    
and access the process engine from the extension object:

    RuntimeService runtimeService = extension.getProcessEngine().getRuntimeService(); 
    
