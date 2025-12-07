# Spring Lab

To create a new Spring sample project

```
NUMBER=05
TITLE=uploading-files
NAME=${NUMBER}-${TITLE}
SHORT_NAME=files
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

```
TITLE=interview-guide
NAME=${TITLE}
SHORT_NAME=interview
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
