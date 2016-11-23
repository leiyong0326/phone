# phone
# 本项目使用jdk1.8开发,请确保运行环境为jdk1.8
# 本项目默认为单机部署,即只有phone-admin一个war包,若需要使用dubbo发布,需加入dubbo配置文件,并将service作为war包使用,后续会讲到.
本项目架构采用Mybatis+Spring+SpringMVC作为主框架
使用apache-shiro作为安全登录及权限验证框架
使用fastjson作数据处理
使用slf4j+log4j做日志记录
使用druid做连接池及监控
采用mysql作为数据库
使用kaptcha并二次开发作为验证码
使用maven作为jar管理
使用redis作为缓存

项目亮点:
采用redis作为主键生成策略,通用性相当不错...
强大的代码生成器助你迅速开发,配合mybatisGenerate使用,简单增删改查无需再写任何代码,所有常用方法一键生成,so easy..
单表查询无需再写mybatis的配置文件,一个selectExtend解决大部分查询条件

# FAQ:

# 为什么说简单增删改查无需写任何代码
代码生成器可以解决大部分常用功能代码编写,常用注释编写,助你统一项目代码架构风格,generate项目为此而生

# 代码生成器怎么用?
# GenerateMybatisGenerateConfig用于自动生成mybatisGenerate的配置文件,当然这个大部分时候并没有什么卵用..
mybatis自身支持所有表生成,使用%即可,不过这个生成器的诞生是因为我并没有去研究过mybatisGenerate,不过这个生成了删配置文件肯定比你删各种model,mapper要爽的多咯..
mybatisGenerate生成所有表示例配置:
<table tableName="%" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false" enableUpdateByExample="false"></table>
注:代码生成器纯java编写,由于多次重构,或有些无用的代码未删除,但不影响使用,还望不要介意,我这人太TM懒了.
# GenerateServiceAndAction用于自动生成除Model和 Mapper.xml以外的所有代码,当然,前提是你的model是存在的,而且PK是作为主键的(你也可以自己改)
BaseGenerate:项目名配置,信息打印开关,写入文件还是打印控制台等
BaseScanBeanGenerate:各个包名的配置, 各个对象父类配置
GenerateServiceAndAction:信息打印开关,打印Dubbo配置文件,文件生成开关(控制是否生成Mapper,Service,ConsumerService,Proxy,Controller)

# 如何支持dubbo方式发布项目?
1.GenerateServiceAndAction配置IS_DUBBO为true,或者自己手动修改service的@Service注解为dubbo的注解
2.加入dubbo配置文件(service(提供者),consumerService(消费者)两边都需要配置,具体百度,网上很多),这里我就不列了
3.安装zookeeper,研究不深,就会玩dubbo+zookeeper 套路.

# 特别提醒
数据验证请在Proxy中完善checkData方法
条件查询请在Proxy中完善getConditions方法,使用条件查询务必在SysUserMapper.xml中复制一份selectExtend的sql,记得修改表名哦

QQ群:
深圳Java程序员 199267671
深圳Java(自学交流) 99281683
