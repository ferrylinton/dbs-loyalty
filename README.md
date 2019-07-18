# DBS Loyalty

## Run With Embedded Server

```
mvn spring-boot:run
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
nohup mvn spring-boot:run &
nohup java -jar target/loyalty-0.0.1-SNAPSHOT.war &
```

## Liquibase
```
mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/000_init_data.xml

mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/002_init_data.xml -Dliquibase.diffTypes=data
```