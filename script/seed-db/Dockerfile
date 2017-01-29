FROM mongo

# Set the location of the data
ENV DATA_HOME /usr/data

# Set data filename aliases
ENV CREDENTIALS credentials.json
ENV HISTORIES histories.json
ENV USERS users.json
ENV URLS urls.json

# Set database name
ENV DB_NAME urss

# Set database host
ENV DB_HOST 127.0.0.1

# Set database port
ENV DB_PORT 27017

# Change directory
WORKDIR $DATA_HOME

# Mount data into container
COPY $CREDENTIALS $DATA_HOME/$CREDENTIALS
COPY $HISTORIES $DATA_HOME/$HISTORIES
COPY $USERS $DATA_HOME/$USERS
COPY $URLS $DATA_HOME/$URLS

# Mount data into database
ENTRYPOINT ["sh", "-c"]
CMD ["mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection credentials --type json --file $DATA_HOME/$CREDENTIALS --jsonArray --upsert && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection histories --type json --file $DATA_HOME/$HISTORIES --jsonArray --upsert && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection users --type json --file $DATA_HOME/$USERS --jsonArray --upsert && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection urls --type json --file $DATA_HOME/$URLS --jsonArray --upsert"]
