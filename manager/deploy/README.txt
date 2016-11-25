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


接口说明：
1. 注册接口
    /arc/user/register
    返回：
    {"code":0}
2. 登录接口
    /arc/user/login
    参数：message={"userName":"ji","password":"123"}
    返回：{"code":0,"obj":{"avatar":"/media/ji/document/school/arc/photo/1.jpeg","balance":0,"id":1,"password":"123","userName":"ji"}}

3. 获取头像借口
    /arc/user/getImage
    参数：url=/media/ji/document/school/arc/photo/1.jpeg
    返回：图片流

4.获取用户余额
    /arc/user/geBalance
    参数：userId=1
    返回：{"code":0,"obj":{"avatar":"/media/ji/document/school/arc/photo/1.jpeg","balance":0,"id":1,"password":"123","userName":"ji"}}

5. 创建计划
    /arc/plan/addPlan
    参数：message={"planName":"testPlan","startTime":"2016-11-10","endTime":"2016-11-30","money":"200","userId":1}
    返回：{"code":0}

6. 获取计划列表
    /arc/plan/getPlanList
    返回：{"code":0,"rows":[{"endTime":"2016-11-30 00:00:00","id":1,"money":200,"planName":"testPlan","startTime":"2016-11-10 00:00:00","status":1,"supervisors":[{"avatar":"/media/ji/document/school/arc/photo/1.jpeg","id":1,"userName":"ji"}],"userAvatar":"/media/ji/document/school/arc/photo/1.jpeg","userId":1,"userName":"ji"}]}

7. 获取计划详情
    /arc/plan/getPlanDetail
    参数：message={"planId":1}
    返回：{"code":0,"obj":{"endTime":"2016-11-30 00:00:00","id":1,"money":200,"planName":"testPlan","startTime":"2016-11-10 00:00:00","status":1,"supervisors":[{"avatar":"/media/ji/document/school/arc/photo/1.jpeg","id":1,"userName":"ji"}],"userAvatar":"/media/ji/document/school/arc/photo/1.jpeg","userId":1,"userName":"ji"},"rows":[{"id":1,"planDate":"2016-11-25 00:00:00","planId":1,"status":1},{"id":2,"planDate":"2016-11-26 00:00:00","planId":1,"status":1},{"id":3,"planDate":"2016-11-27 00:00:00","planId":1,"status":1},{"id":4,"planDate":"2016-11-28 00:00:00","planId":1,"status":1},{"id":5,"planDate":"2016-11-29 00:00:00","planId":1,"status":1},{"id":6,"planDate":"2016-11-30 00:00:00","planId":1,"status":1}]}

8. 监督某个计划（加入某个计划）
    /arc/plan/joinPlan
    参数：message={"planId":1,"userId":1}
    返回：{"code":0}

9. 给计划项上传证据
    /arc/plan/addEvidence
    参数：
        planItemId=1&comment="今天跑了10KM"     (其中comment可以不填)
        还有file,图片流
    返回：{"code":0}

10. 质疑某个计划项（反对某个计划项）
    /arc/judge/judge
    参数：message={"userId":1,"planItemId":1,"judge":1}
    返回：{"code":0}

