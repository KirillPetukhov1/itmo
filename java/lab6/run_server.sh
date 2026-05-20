#!/bin/sh

if [ -z "$LAB5_FILENAME" ]; then
    echo "Error: LAB5_FILENAME environment variable is not set."
    echo "Usage: LAB5_FILENAME=/prog_java/lab6/collection.xml sh run_server.sh"
    exit 1
fi

LAB6_PORT="${LAB6_PORT:-6377}"

JAR="server/build/libs/server-1.0-SNAPSHOT-fat.jar"

if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found. Run build.sh first."
    exit 1
fi

unset _JAVA_OPTIONS

java \
    -Xmx512m \
    -XX:MaxMetaspaceSize=256m \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -DLAB5_FILENAME="$LAB5_FILENAME" \
    -DLAB6_PORT="$LAB6_PORT" \
    -jar "$JAR"
