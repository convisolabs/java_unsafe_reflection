#!/bin/bash

find ./src/com/testcases -name "*.java" -print | xargs javac -cp lib/* -d .

