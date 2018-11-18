到database的lib目录执行以下命令以安装sql server jdbc driver 4 到本地maven目录
```
mvn install:install-file -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.2 -Dpackaging=jar -Dfile=sqljdbc42.jar
```