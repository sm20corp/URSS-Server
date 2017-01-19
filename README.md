# URSS-Server
URSS Server, fetch RSS feeds (Java, Vert.x, MongoDB, Docker, JUnit, javadoc, ROME for RSS feeds).

# Maven powered
Follow these instructions from the root of this repository to set it up and running.
* Set a $SECRET env variable
* Generate JWT key "$> keytool -genseckey -keystore keystore.jceks -storetype jceks -storepass $SECRET -keyalg HMacSHA256 -keysize 2048 -alias HS256 -keypass $SECRET"
* Run a local database "$> mongod &"
* Build and test "$> mvn clean package"
* Run "$> java -jar target/Server-1.0.0-fat.jar"

# Docker powered
Follow these instructions from the root of this repository to deploy this server on containers.
* Install docker-engine and docker-compose on your operating system
* Set a $SECRET env variable
* Generate JWT key "$> keytool -genseckey -keystore keystore.jceks -storetype jceks -storepass $SECRET -keyalg HMacSHA256 -keysize 2048 -alias HS256 -keypass $SECRET"
* Build and test "$> mvn clean package"
* Build images "$> docker-compose build"
* Deploy "$> docker-compose up"

---

__Epitech project__
