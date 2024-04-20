@echo off
mkdir classes 2>nul
javac -d classes com\piscan\zealot\*.java
java -cp classes com.piscan.zealot.Zealot %1
