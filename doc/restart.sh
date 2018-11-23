#!/bin/bash

bin_path=$(cd `dirname $0`; pwd)
echo "bin_path : $bin_path"

cd $bin_path

echo "bash stop.sh"
bash ./stop.sh 

if [ $? -eq -1 ]
then
    exit
fi

echo "exec start.sh"
exec ./start.sh
