# Ubuntu with Artemis Server image
FROM apache/activemq-artemis:latest

# Use root for file ownership/permission changes
USER root

# Copy the broker.xml into artemis etc folder
COPY assets /assets/artemis

# executables
RUN chmod -R a+rwx /assets/artemis

# Switch back to the default runtime user
USER artemis

#CMD /scripts/artemis/start-container.sh
CMD ["/bin/artemis-service", "start"]
