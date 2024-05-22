## HOW TO RUN

- clon the repository
```git clone git@github.com:SuperFranklin/cron-expression-parser.git```
- enter project directory
```cd cron-expression-parser```
- build jar file 
```./gradlew clean build```
- run jar file
```java -jar build/libs/cron-expression-parser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"```

## PROGRAM ARGUMENTS

- The program takes a single argument as a string. The string is a cron expression with 6 fields. The fields are separated by a space. The fields are as follows:
    - Minute (0 - 59)
    - Hour (0 - 23)
    - Day of the month (1 - 31)
    - Month (1 - 12)
    - Day of the week (1 - 7)
    - Command (string)

## REQUIREMENTS

- Java 17 or higher