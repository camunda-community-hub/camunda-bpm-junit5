# Register Your Process Engine in JUnit 5 and Camunda Platform Assert

This examples shows how to create a process engine in your test class without a configuration file and register it for your test runs.

It uses camunda-bpm-assert to run comprehensive tests.

The project contains the following files:

```
src
├── main
│   ├── java
│   └── resources
└── test
    ├── java
    │   └── org
    │       └── camunda
    │           └── bpm
    │               └── unittest
    │                   └──engine
    │                      └── UseProcessEngineTest.java   (1)
    └── resources
        └── testProcess.bpmn                               (2)
```
Explanation:
* (1) The test class that
    * creates the process engine from the configuration builder
    * registers the process engine for the JUnit 5 test
    * registers the process engine for the camunda-bpm-assert library
* (2) The process model to test

## Running the test with maven

In order to run the test with maven you can say:

```
mvn clean test
```

## Importing the project into eclipse.

If you use eclipse you can simply import the project by selecting `File / Import |-> Existing Maven Projects.

[assert]: https://github.com/camunda/camunda-bpm-assert

## License
The source files in this repository are made available under the [Apache License Version 2.0](../../LICENSE). 