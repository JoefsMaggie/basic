#!/bin/bash

bin_path=$(cd `dirname $0`; pwd)
echo "bin_path : $bin_path"

cd $bin_path
cd ..
cd lib

lib_path=`pwd`
echo "lib_path : $lib_path"

jar_name=`ls -t | grep .jar$ | awk 'NR==1'`
echo "jar_name : $jar_name"

pid=`ps -ef | grep $jar_name | grep -v "grep" | awk '{print $2}'`

if [ $pid ]; then  
    echo "${jar_name} is running and pid = $pid"  
    kill -9 $pid  
    if [[ $? -eq 0 ]];then   
       echo "success to stop $jar_name "   
    else   
       echo "failure to stop $jar_name " 
       exit 1
    fi  
fi 
