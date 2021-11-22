# Open Data Discovery Resource Name Generator
## Requirements
Java >= 11
## Installation
```
TBD
```
## Usage and configuration
### Available generators
* postgresql - PostgreSqlPath
* mysql - MysqlPath
* kafka - KafkaPath
* kafkaconnect - KafkaConnectorPath
* snowflake - SnowflakePath
* airflow - AirflowPath
* hive - HivePath
* dynamodb - DynamodbPath
* grpc - GrpcServicePath

### Generator methods
* generate(OddrnPath path, String field) - Get oddrn string by path.
* validate(OddrnPath path, String field) - Validate oddrn path

### Example usage
```java
# postgresql

final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .table("test")
        .tableColumn("id")
        .build(),
    , "tableColumn");

# //postgresql/host/1.1.1.1/databases/dbname/schemas/public/tables/test/columns/id

```

### Exceptions
* WrongPathOrderException - raises when trying set path that depends on another path
```java
final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .tableColumn("id")
        .build(),
    , "tableColumn");
# WrongPathOrderException: 'column' can not be without 'table' attribute
```
* PathDoestExistException - raises when trying to get not existing oddrn path
```java
final String oddrn = generator.generate(
    PostgreSqlPath.builder()
        .host("1.1.1.1")
        .database("dbname")
        .schema("public")
        .tableColumn("id")
        .build(),
    , "job");

# PathDoestExistException: Path 'job' doesn't exist in path
```