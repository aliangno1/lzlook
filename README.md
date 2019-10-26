# 种质信息获取接口使用说明

1. 电脑配置java开发环境(就是jdk8以上)

2. 启动项目

3. http://localhost:8080/seed/search?id=
   (等号后接种质id，则可查询到该种质信息)

4. http://localhost:8080/seed/export 
   此接口调用需***首先***在D盘根目录下创建ids.txt，否则报错
   ids.txt文件中内容如下：

   ```
   ["09-00782"],["09-00785"],["09-00790"]
   ```

   文件写好之后，调用接口，则会在D盘根目录下创建export.xls文件

