mongod &
java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Xss512k -XX:+UseSerialGC \
 -Djava.security.egd=file:/dev/./urandom -jar /app/researchengine-0.0.1-SNAPSHOT.jar --server.port=$PORT