# Camunda BPM Assert Example for JUnit 5

This project contains a simple example of how to write a unit test for Camunda BPM with JUnit 5. 

The project contains the following files:

```
src/
├── main
│   ├── java
│   └── resources
└── test
    ├── java
    │   └── org
    │       └── camunda
    │           └── bpm
    │               └── unittest
    │                   └── SimpleTestCase.java   (1)
    └── resources
        ├── camunda.cfg.xml                       (2)
        └── testProcess.bpmn                      (3)
```
Explanation:

* (1) A java class containing a JUnit Test. It uses the `ProcessEngineExtension` for bootstrapping the process engine, as well as [camunda-bpm-assert][assert] to make your test life easier.
* (2) Configuration file for the process engine.
* (3) An example BPMN process.

## Running the test with maven

In order to run the testsuite with maven you can say:

```
mvn clean test
```

## Importing the project into eclipse.

If you use eclipse you can simply import the project by selecting `File / Import |-> Existing Maven Projects.

[assert]: https://github.com/camunda/camunda-bpm-assert

## License
The source files in this repository are made available under the [Apache License Version 2.0](../../LICENSE).