执行以下命令将jar安装到本地仓库
```
mvn install:install-file -DgroupId=com.ctrip.framework.apollo -DartifactId=apollo-core -Dversion=0.7.0 -Dpackaging=jar -Dfile=apollo-core-0.7.0.jar
mvn install:install-file -DgroupId=com.ctrip.framework.apollo -DartifactId=apollo-core -Dversion=0.7.0 -Dpackaging=jar -Dfile=apollo-core-0.7.0-sources.jar -Dclassifier=sources
mvn install:install-file -DgroupId=com.ctrip.framework.apollo -DartifactId=apollo-client -Dversion=0.7.0 -Dpackaging=jar -Dfile=apollo-client-0.7.0.jar
mvn install:install-file -DgroupId=com.ctrip.framework.apollo -DartifactId=apollo-client -Dversion=0.7.0 -Dpackaging=jar -Dfile=apollo-client-0.7.0-sources.jar -Dclassifier=sources
```

