#!/bin/bash

java -cp ".:libs/*" PostgreSQLUnsafeReflection "jdbc:postgresql://localhost:5432/lol?socketFactory=$1&socketFactoryArg=$2"
