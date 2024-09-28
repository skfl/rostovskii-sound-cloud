FROM gradle:8.5.0-jdk17-focal as build

COPY . /app
WORKDIR /app
RUN gradle detekt build --no-daemon

FROM amazoncorretto:17-alpine-jdk as production

EXPOSE 8080 8081

ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -Dfile.encoding=UTF8 \
  -XX:+UseCompressedOops \
  -XX:+UseStringDeduplication \
  -XX:+UseCompressedClassPointers \
  -XX:-HeapDumpOnOutOfMemoryError \
  -XX:+AlwaysActAsServerClassMachine \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+PerfDisableSharedMem \
  -XX:+AlwaysPreTouch \
  -Dspring.profiles.active=docker"

COPY --from=build /app/build/libs/app-1.0.0.jar /app.jar

ENTRYPOINT ["/bin/sh","-c", "java -version && exec java $JAVA_OPTS -jar app.jar"]
