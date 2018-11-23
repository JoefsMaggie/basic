#!/bin/bash

# apollo 配置设置
apollo_env=dev                                   # apollo 环境
apollo_app_id=tags_test                          # apollo appId
apollo_meta_service=http://192.168.5.219:8080    # apollo 配置的请求路径
apollo_cache_folder=/opt/dev/services/tags       # apollo 缓存路径，0.7.0版本不支持缓存目录的定义，此项可忽略
# spring 配置设置
spring_profile_active=test                       # spring 配置环境文件
# 日志相关配置
log_level=INFO                                   # 日志级别

function green() {
    echo -e "\033[32m$1 \033[0m"
}

function red() {
    echo -e "\033[31m$1 \033[0m"
}

echo "apollo 环境 : $apollo_env"
echo "apollo appId : $apollo_app_id"
echo "apollo 配置中心 : $apollo_meta_service"
echo "apollo 缓存路径 : $apollo_cache_folder"
echo "spring 配置使用 : $spring_profile_active"
echo "logback 日志级别 : $log_level"

bin_path=$(cd `dirname $0`; pwd)
echo "bin_path : $bin_path"

cd $bin_path
cd ../lib

lib_path=`pwd`
echo "lib_path : $lib_path"

jar_name=`ls | grep .jar`
echo "jar_name : $jar_name"

pid=`ps -ef | grep $jar_name | grep -v "grep" | awk '{print $2}'`
if [ $pid ]; then
    green "app is running and pid = $pid"
    exit
fi

echo "starting $jar_name ..."

GC_CONF="-XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Xloggc:../logs/gc.log"


cmd="java $GC_CONF 
-Denv=$apollo_env -Dapp.id=$apollo_app_id 
-D${apollo_env}_meta=$apollo_meta_service 
-Dapollo.cacheDir=$apollo_cache_folder 
-Dlogging.level.root=$log_level 
-jar $lib_path/$jar_name 
--spring.profiles.active=$spring_profile_active "

echo -e "execute cmd: \nnohup $cmd > /dev/null 2>&1 & \n"
nohup $cmd > /dev/null 2>&1 &

pid=`ps -ef | grep $jar_name | grep -v "grep" | awk '{print $2}'`

if [ $pid ]; then
    green "${jar_name} start success and pid = $pid"
else
    red "${jar_name} start failure"
fi
