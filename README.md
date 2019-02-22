# MyBlog


## Liquibase
```
mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/000_init_data.xml

mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/release-001/002_init_data.xml -Dliquibase.diffTypes=data
```


## Run With Embedded Tomcat 7

```
mvn tomcat7:run
mvn clean tomcat7:run -Dmaven.tomcat.port=8181
```
## Build

### Minify html
```
mvn htmlcompressor:html
```

### Build war
```
mvn clean package
```