#!/bin/sh
javac -sourcepath src/ -d build/ `find src/ -name '*.java'`
