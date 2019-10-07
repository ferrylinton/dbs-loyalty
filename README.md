# DBS Loyalty

## Run With Embedded Server

```
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=local,cdnjs,min
mvn spring-boot:run -Dspring-boot.run.profiles=vps,cdnjs,min
```
## Deploy

### Minify HTML
```
mvn htmlcompressor:html
```

### Build war
```
mvn clean package -DskipTests
```

### Copy To Server
```
scp target/loyalty-0.0.1-SNAPSHOT.war root@192.227.166.217:/loyalty
```

### Run
```
java -jar target/loyalty-0.0.1-SNAPSHOT.war --spring.profiles.active=local,cdnjs,min
nohup mvn spring-boot:run &
nohup java -jar target/loyalty-0.0.1-SNAPSHOT.war &
nohup java -jar target/loyalty-0.0.1-SNAPSHOT.war --spring.profiles.active=vps,cdnjs,min &
```

## Liquibase
```
mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/000_init_data.xml

mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/002_init_data.xml -Dliquibase.diffTypes=data
```