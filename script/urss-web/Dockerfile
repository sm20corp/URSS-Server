FROM node

ENV HOME /usr/urss

ENV URSS_PATH src

EXPOSE 8000

COPY $URSS_PATH $HOME/

WORKDIR $HOME/$URSS_PATH

RUN npm install http-server

# Mount data into database
ENTRYPOINT ["sh", "-c"]
CMD ["npm install && \
      npm start"]
