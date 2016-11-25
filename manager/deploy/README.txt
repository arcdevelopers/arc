1. 安装好mysql
2. 解压dep.zip包
3. arc.sql是建表语句。在mysql中创建一个库，用arc.sql把表建好
4. 修改jdbc.properties文件中的数据连接部分：
    jdbc.url = jdbc:mysql://127.0.0.1:3306/arc?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
    jdbc.username = root
    jdbc.password = root
5. 进入到dep目录
6. 启动命令 ./deploy.sh start
7. 终止命令 ./deploy.sh stop