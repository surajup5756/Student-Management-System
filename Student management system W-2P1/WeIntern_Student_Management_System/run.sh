#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
echo "Compiling Student Management System..."
javac -encoding UTF-8 -d out src/main/java/com/weintern/sms/*.java
echo "Starting Student Management System..."
java -cp out com.weintern.sms.Main
