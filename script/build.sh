export SECRET=secret
keytool -genseckey -keystore keystore.jceks -storetype jceks -storepass $SECRET -keyalg HMacSHA256 -keysize 2048 -alias HS256 -keypass $SECRET
mvn clean package -Dmaven.test.skip=true
