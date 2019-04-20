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