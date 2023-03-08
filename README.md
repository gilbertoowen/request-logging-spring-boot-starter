# request-logging
## About The Project
This is a HTTP logging with Spring Boot
## Getting Started

1. add maven dependency

```xml
<dependency>
    <groupId>io.github.gilbertoowen</groupId>
    <artifactId>request-logging-spring-boot-starter</artifactId>
    <version>1.3.RELEASE</version>
</dependency>
```

2. add config

```yaml
request.logging.enabled: true
```

## configuration

| name                       |type| desc                                            |
|----------------------------|---|-------------------------------------------------|
| request.logging.whiteList  |list| url do log, support ant path style patterns     |
| request.logging.blackList  |list| url do not log, support ant path style patterns |
| request.logging.logHeaders |boolean| log header or not, default false                |


