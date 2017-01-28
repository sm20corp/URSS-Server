FROM mongo

# Set the location of the data
ENV DATA_HOME /usr/data

# Set data filename aliases
ENV CREDENTIALS credentials.json
ENV ARTICLES articles.json
ENV FEEDS feeds.json
ENV HISTORIES histories.json
ENV USERS users.json

# Change directory
WORKDIR $DATA_HOME

# Mount credentials data into database
COPY $CREDENTIALS $DATA_HOME/$CREDENTIALS
CMD mongoimport --host mongodb --db reach-engine --collection credentials --type json --file $DATA_HOME/$CREDENTIALS --jsonArray

# Mount articles data into database
COPY $ARTICLES $DATA_HOME/$ARTICLES
CMD mongoimport --host mongodb --db reach-engine --collection articles --type json --file $DATA_HOME/$ARTICLES --jsonArray

# Mount feeds data into database
COPY $FEEDS $DATA_HOME/$FEEDS
CMD mongoimport --host mongodb --db reach-engine --collection feeds --type json --file $DATA_HOME/$FEEDS --jsonArray

# Mount histories data into database
COPY $HISTORIES $DATA_HOME/$HISTORIES
CMD mongoimport --host mongodb --db reach-engine --collection histories --type json --file $DATA_HOME/$HISTORIES --jsonArray

# Mount users data into database
COPY $USERS $DATA_HOME/$USERS
CMD mongoimport --host mongodb --db reach-engine --collection users --type json --file $DATA_HOME/$USERS --jsonArray
