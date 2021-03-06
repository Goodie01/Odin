# Odin
![Build](https://github.com/Goodie01/Odin/workflows/Build/badge.svg)
![Release](https://github.com/Goodie01/Odin/workflows/Release/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Goodie01_Odin&metric=alert_status)](https://sonarcloud.io/dashboard?id=Goodie01_Odin)

Odin is designed as a simple easy to use Data access layer for use in quickly creating/mocking up applications, and giving them a quick and easy persistence layer without to much thought or difficulty.

Odin is often associated with wisdom, healing, royality, the gallows, and knowledge. The latter is why this library is named after him.

## Odin Version 1.X should be considered beta with a API subject to change at a whim

# Example usage
```java
    class Main {
        public static void main(String[] args){
            Odin odin = Odin.create()
                    .addPackageName("org.goodiemania.odin.entities")
                    .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                    .build();
            EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);
            Optional<ExampleEntity> entity = em.getById("cdf91e4e-030e-11eb-adc1-0242ac120002");  
        }
    }
```
For further details see SqLiteExamples under src/test/java/org/goodiemania/odin/SqLiteExamples.java

# Requirements
All objects should be well-formed java beans, with getters and setters as you expect. There should be a no args constructor available. Variables should not be final. Basically we support everything Jackson does natively. 

# Misc

Running mutation tests
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

# Misc links
* [Odin Sonar](https://sonarcloud.io/dashboard?id=Goodie01_Odin)