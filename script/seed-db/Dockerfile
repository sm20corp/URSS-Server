FROM mongo

# Set the location of the data
ENV DATA_HOME /usr/data

# Set data filename aliases
ENV CREDENTIALS credentials.json
ENV ARTICLES articles.json
ENV FEEDS feeds.json
ENV HISTORIES histories.json
ENV USERS users.json

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
COPY $ARTICLES $DATA_HOME/$ARTICLES
COPY $FEEDS $DATA_HOME/$FEEDS
COPY $HISTORIES $DATA_HOME/$HISTORIES
COPY $USERS $DATA_HOME/$USERS

# Mount data into database
ENTRYPOINT ["sh", "-c"]
CMD ["mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection credentials --type json --file $DATA_HOME/$CREDENTIALS --jsonArray && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection articles --type json --file $DATA_HOME/$ARTICLES --jsonArray && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection feeds --type json --file $DATA_HOME/$FEEDS --jsonArray && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection histories --type json --file $DATA_HOME/$HISTORIES --jsonArray && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection users --type json --file $DATA_HOME/$USERS --jsonArray"]
