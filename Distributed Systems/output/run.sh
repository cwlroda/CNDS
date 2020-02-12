#! /bin/bash
INPUT=data/test.csv
IFS=","

[ ! -f $INPUT ] && { echo "$INPUT file not found!"; exit 99; }
while read field1 field2 field3
do
    ./output/screen_dump $field1 $field2 $field3;
    echo ""
done < $INPUT