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

11. 获取计划详情
    /arc/plan/getPlanDetail
    参数：message={"planId":1}
    返回：
    {"code":0,"obj":{"endTime":"2016-11-30 00:00:00","id":1,"money":200,"planName":"testPlan","startTime":"2016-11-10 00:00:00","status":3,"supervisors":[{"avatar":"/media/ji/document/school/arc/photo/1.jpeg","id":1,"userName":"ji"}],"userAvatar":"/media/ji/document/school/arc/photo/1.jpeg","userId":1,"userName":"ji"},"rows":[{"id":1,"planDate":"2016-11-25 00:00:00","planId":1,"status":3},{"id":2,"planDate":"2016-11-26 00:00:00","planId":1,"status":1},{"id":3,"planDate":"2016-11-27 00:00:00","planId":1,"status":1},{"id":4,"planDate":"2016-11-28 00:00:00","planId":1,"status":1},{"id":5,"planDate":"2016-11-29 00:00:00","planId":1,"status":1},{"id":6,"planDate":"2016-11-30 00:00:00","planId":1,"status":1}]}

12. 获取某个计划项的证据
    /arc/plan/getEvidence
    参数：message={"planItemId":1}
    返回：{"code":0,"rows":[{"avatar":"/media/ji/document/school/arc/evidence/1_1480077946965/idea.jpg","comment":"测试评论","created":"2016-11-25 20:45:46","id":1,"planItemId":1}]}



系统说明
1. 创建计划，只能创建今天及以后的计划：
    (1）如果开始和结束时间都早于几天，无法创建
    (2) 如果开始时间早于今天，结束时间晚于今天，计划项从今天开始，早于今天的不创建计划项
2. 账户余额说明
    (1) 用户创建计划，会输入一个金额，但不会直接从账户中扣减，原因是：现实场景中用户的支付方式有很多，比如微信和银行卡，不一定非要用app账户支付
    (2) 用户创建的计划如果失败了，会直接将计划的钱平分，平分的钱直接打入监督者账户
3. 计划结果说明
    (1) 对于整个计划来说,如果计划中的任何一天的计划项失败了，整个计划失败
    (2) 对于计划项来说，如果超过50%的人反对，那么计划项失败
    (3) 对于计划项来说，监督者没有反对（质疑）,则认为改监督者是认同的
    (4) 每天24点会对当天的任务做一个处理，没有反对者或反对者小于50%的，则计划项成功，当所有计划项都成功，改计划成功，计划成功后，押金将返还到用户账户余额