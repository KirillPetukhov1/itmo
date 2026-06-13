#!/bin/sh
./gradlew :server:fatJar :client:fatJar --no-daemon
echo ""
echo "Build complete."
echo "Server jar: server/build/libs/server-1.0-SNAPSHOT-fat.jar"
echo "Client jar: client/build/libs/client-1.0-SNAPSHOT-fat.jar"
