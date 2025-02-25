#!/bin/bash
javac --module-path ../javafx-sdk-17.0.13/lib --add-modules javafx.controls -cp "lib/*" -d bin src/models/*.java src/application/*.java src/storage/*.java src/components/*.java
java --module-path ../javafx-sdk-17.0.13/lib --add-modules javafx.controls -cp "bin:lib/*" application.Main

