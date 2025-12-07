# Spring Lab

To create a new Spring sample project

```
NUMBER=04
TITLE=relational-data-access
NAME=${NUMBER}-${TITLE}
SHORT_NAME=relational
spring init \
  --name=${NAME} \
  --artifactId=${TITLE} \
  --groupId=com.example \
  --packageName=com.example.${SHORT_NAME} \
  --build=maven \
  --language=java \
  --dependencies=jdbc \
  ${NAME}
```
