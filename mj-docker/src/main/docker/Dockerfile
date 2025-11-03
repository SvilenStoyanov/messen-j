FROM eclipse-temurin:21-jre-jammy

ENV JAR_LOCATION="/data/messen-j/server.jar"

#COPY ../../docker.dev.brandmaker.com/messenger-image/1.0.0-SNAPSHOT/build/server.jar $JAR_LOCATION
COPY mj-server.jar $JAR_LOCATION
COPY scripts /scripts

RUN chmod -R a+x /scripts/*.sh \
    && mkdir -p /runtime/logs \
    && chmod a+rw /runtime/logs

HEALTHCHECK --interval=3s --retries=5 \
  CMD curl -f http://localhost:8080/api/health || exit 1

ENTRYPOINT [ "/bin/bash", "/scripts/run.sh" ]
