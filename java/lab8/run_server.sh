#!/bin/sh

if [ -z "$DB_USER" ]; then
    echo "Error: DB_USER environment variable is not set."
    exit 1
fi

if [ -z "$DB_PASSWORD" ]; then
    echo "Error: DB_PASSWORD environment variable is not set."
    exit 1
fi

LAB6_PORT="${LAB6_PORT:-6377}"

JAR="server/build/libs/server-1.0-SNAPSHOT-fat.jar"

if [ ! -f "$JAR" ]; then
    JAR="server-1.0-SNAPSHOT-fat.jar"
    if [ ! -f "$JAR" ]; then
        echo "Error: $JAR not found. Run build.sh first."
        exit 1
    fi
fi

unset _JAVA_OPTIONS

java \
    -Xmx512m \
    -XX:MaxMetaspaceSize=256m \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -DLAB6_PORT="$LAB6_PORT" \
    -DB_USER="$DB_USER" \
    -DB_PASSWORD="$DB_PASSWORD" \
    -jar "$JAR"
