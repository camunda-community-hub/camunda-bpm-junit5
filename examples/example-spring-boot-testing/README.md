# Test camunda-bpm-spring-boot-starter with JUnit 5

This examples shows how to test a process automated with the camunda-bpm-spring-boot-starter using JUnit 5. This extension is **not required**.

Have a look into the [pom.xml](pom.xml) for the complete setup.

JUnit 5 is imported from the `spring-boot-starter-test` dependency. 

The spring framework allows to use the engine configuration from the `application.yaml` in the tests. To keep the database clean from the test runs, the tests uses an in-memory database and disable the job executor to make the tests reliable.

```
@SpringBootTest(args = {
    "--spring.datasource.url=jdbc:h2:mem:camunda;DB_CLOSE_ON_EXIT=FALSE",  
    "--camunda.bpm.job-execution.enabled=false"
    })
public class WorkflowTest {
```
 
To interact with the engine, the services gets injected:

```
  @Autowired
  public RuntimeService runtimeService;

```

Assertions from `camunda-bpm-assert` can be used to write comprehensive tests.

See the [WorkflowTest](src/main/test/org/camunda/springboottest/WorkflowTest.java) for the complete example.
