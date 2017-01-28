FROM java:8u77-jre-alpine

ENV VERTICLE_FILE Server-1.0.0-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

EXPOSE 8080

# Copy your fat jar to the container
COPY target/$VERTICLE_FILE $VERTICLE_HOME/

# Set secret env variable
ENV SECRET secret

# Change directory
WORKDIR $VERTICLE_HOME

# Create JWT key
RUN keytool -genseckey -keystore keystore.jceks -storetype jceks -storepass ${SECRET} -keyalg HMacSHA256 -keysize 2048 -alias HS256 -keypass ${SECRET}

# Launch the verticle
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $VERTICLE_FILE"]
