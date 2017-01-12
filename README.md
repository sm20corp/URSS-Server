# URSS-Server
URSS Server, fetch RSS feeds (Java, Vert.x, MongoDB, Docker, JUnit, javadoc, ROME for RSS feeds).

# Maven powered
Follow these instructions from the root of this repository to set it up and running.
* Set a $SECRET env variable
* Replace $SECRET with the real secret value in this command to generate the jwt key: "$> keytool -genseckey -keystore keystore.jceks -storetype jceks -storepass $SECRET -keyalg HMacSHA256 -keysize 2048 -alias HS256 -keypass $SECRET"
* Run a local database "$> mongod &"
* Build and test "$> mvn clean package"
* Run "$> java -jar target/Server-1.0.0-fat.jar"


---

__Epitech project__
