#!/bin/sh

LAB6_HOST="${LAB6_HOST:-localhost}"
LAB6_PORT="${LAB6_PORT:-6377}"

JAR="client/build/libs/client-1.0-SNAPSHOT-fat.jar"

if [ ! -f "$JAR" ]; then
    JAR="client-1.0-SNAPSHOT-fat.jar"
    if [ ! -f "$JAR" ]; then
        echo "Error: $JAR not found. Run build.sh first."
        exit 1
    fi
fi

unset _JAVA_OPTIONS

java \
    -Xmx256m \
    -XX:MaxMetaspaceSize=128m \
    -DLAB6_HOST="$LAB6_HOST" \
    -DLAB6_PORT="$LAB6_PORT" \
    -jar "$JAR"
