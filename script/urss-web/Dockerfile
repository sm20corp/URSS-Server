FROM node

EXPORT 8000

# Mount data into database
ENTRYPOINT ["npm"]
CMD ["mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection credentials --type json --file $DATA_HOME/$CREDENTIALS --jsonArray --upsert && \
      mongoimport --host $DB_HOST --port $DB_PORT --db $DB_NAME --collection histories --type json --file $DATA_HOME/$HISTORIES --jsonArray --upsert &&"]
