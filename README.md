# DBS Loyalty

## Run With Embedded Server (pick one command)

```
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=local,cdnjs,min
mvn spring-boot:run -Dspring-boot.run.profiles=vps,cdnjs,min
nohup mvn spring-boot:run -Dspring-boot.run.profiles=vps,cdnjs,min &
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

### Login To Server
```
ssh root@192.227.166.217
1709b580ed
```

### Copy To Server
```
scp target/loyalty-0.0.1-SNAPSHOT.war root@192.227.166.217:/loyalty
```

### Run Spring Boot At Server
```
nohup java -jar target/loyalty-0.0.1-SNAPSHOT.war --spring.profiles.active=vps,cdnjs,min &
```

### Check Log Nohup
```
tail -1000f nohup.out
```

### Check From Browser
```
http://192.227.166.217:8181/loyalty/login
```

## Liquibase
```
mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/000_init_data.xml

mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/002_init_data.xml -Dliquibase.diffTypes=data
```