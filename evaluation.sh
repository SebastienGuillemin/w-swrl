#!/bin/bash
echo "Compiling program"

mvn clean install

for file in ./evaluation_kb/*.ttl;
do
    echo "Processing $file"

    for i in $(seq 1 $1);
    do
        echo "  Running SWRL" $i
        mvn exec:java -Dexec.mainClass="com.sebastienguillemin.wswrl.evaluation.Evaluation" -Dexec.args="1 SWRL $file" > /dev/null
    done

    echo ""

    for i in $(seq 1 $1);
    do
    echo "  Running WSWRL" $i
        mvn exec:java -Dexec.mainClass="com.sebastienguillemin.wswrl.evaluation.Evaluation" -Dexec.args="1 WSWRL $file" > /dev/null
    done
done