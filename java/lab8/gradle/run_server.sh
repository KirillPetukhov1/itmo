#!/bin/sh

if [ -z "$LAB5_FILENAME" ]; then
    echo "Error: LAB5_FILENAME environment variable is not set."
    echo "Usage: LAB5_FILENAME=/path/to/collection.xml sh run_server.sh"
    exit 1
fi

LAB6_PORT="${LAB6_PORT:-8080}"

JAR="server/build/libs/server-1.0-SNAPSHOT-fat.jar"

if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found. Run build.sh first."
    exit 1
fi

java \
    -Xmx512m \
    -XX:MaxMetaspaceSize=256m \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -DLAB5_FILENAME="$LAB5_FILENAME" \
    -DLAB6_PORT="$LAB6_PORT" \
    -jar "$JAR"
