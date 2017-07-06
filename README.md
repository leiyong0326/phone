# 自述

## 一个高可用,高效率,可重用性和可扩展性都还不错的基础框架
## 本项目使用jdk1.8开发,请确保运行环境为jdk1.8
## 本项目默认为单机部署,即只有phone-admin一个war包,若需要使用dubbo发布,需加入dubbo配置文件,并将service作为war包使用,后续会讲到.
<ul>
<li>本项目架构采用Mybatis+Spring+SpringMVC作为基础框架</li>
<li>使用apache-shiro作为安全登录及权限验证框架</li>
<li>使用fastjson作数据处理</li>
<li>使用slf4j+log4j做日志记录</li>
<li>使用druid做连接池及监控</li>
<li>采用mysql作为数据库</li>
<li>使用kaptcha并二次开发作为验证码</li>
<li>使用maven作为jar管理</li>
<li>使用redis作为缓存</li>
<li>使用poi导出excel</li>
</ul>

# 项目亮点:
<ul>
<li>严格的权限控制,当然你也可以自己扩展..</li>
<li>采用redis作为主键生成策略,无序考虑主键策略,且通用性相当不错.--<a href="http://blog.csdn.net/leiyong0326/article/details/52039200">技术详解</a></li>
<li>AOP加注解方式记录日志,日志内容清晰,记录方式简单.--<a href="http://blog.csdn.net/leiyong0326/article/details/52039086">技术详解</a></li>
<li>简单易用的导出配置,助你5分钟实现导出,详见底部</li>
<li>强大的代码生成器助你快速开发,配合mybatisGenerate使用,简单增删改查无需再写任何代码,所有常用方法一键生成,so easy..</li>
<li>单表查询无需再写mybatis的配置文件,一个selectExtend解决大部分查询条件</li>
</ul>

# 项目来源
<ul>
<li>给朋友做的一个系统,已删除核心代码,保留用户权限管理模块及登录日志和通用工具等模块.</li>
</ul>

# FAQ:

## 为什么说简单增删改查无需写任何代码
<ul>
<li>代码生成器可以解决大部分常用功能代码编写,常用注释编写,助你统一项目代码架构风格,generate项目为此而生</li>
</ul>

## 代码生成器怎么用?

### GenerateMybatisGenerateConfig用于自动生成mybatisGenerate的配置文件,当然这个大部分时候并没有什么卵用..
<ul>
<li>mybatis自身支持所有表生成,使用%即可,不过这个生成器的诞生是因为我并没有去研究过mybatisGenerate,不过这个生成了删配置文件肯定比你删各种model,mapper要爽的多咯..</li>
<li>注:代码生成器纯java编写,由于多次重构,或有些无用的代码未删除,但不影响使用,还望不要介意,我这人太TM懒了.</li>
<li>mybatisGenerate生成所有表示例配置:</li>
</ul>

```xml
<table tableName="%" enableCountByExample="false" enableDeleteByExample="false" enableSelectByExample="false" enableUpdateByExample="false"></table>
```
### GenerateServiceAndAction用于自动生成除Model和 Mapper.xml以外的所有代码,当然,前提是你的model是存在的,而且PK是作为主键的(你也可以自己改)
<ul>
<li>BaseGenerate:项目名配置,信息打印开关,写入文件还是打印控制台等</li>
<li>BaseScanBeanGenerate:各个包名的配置, 各个对象父类配置</li>
<li>GenerateServiceAndAction:信息打印开关,打印Dubbo配置文件,文件生成开关(控制是否生成Mapper,Service,ConsumerService,Proxy,Controller)</li>
</ul>

## 如何支持dubbo方式发布项目?
<ul>
<li>1.GenerateServiceAndAction配置IS_DUBBO为true,或者自己手动修改service的@Service注解为dubbo的注解</li>
<li>2.加入dubbo配置文件(service(提供者),consumerService(消费者)两边都需要配置,具体百度,网上很多),这里我就不列了</li>
<li>3.安装zookeeper,研究不深,就会玩dubbo+zookeeper 套路.</li>
<li>注意:</li>
<li>dubbo的扫描与shiro冲突,所以服务端需要使用配置文件的方式,如果您有更好的解决方案,谢谢留言--<a href="http://blog.csdn.net/leiyong0326/article/details/52036736">详见</a>.</li>
<li>dubbo的扫描与SpringAOP事务冲突(AOP事务和注解方式都有冲突,调试发现AOP后dubbo获取的SpringProxy代理类,而不是dubbo的代理类),提供端需要使用手动事务或加一层壳,使用壳(提供者暴露接口用)调用service(AOP事务层),如果您有更好的解决方案,谢谢留言--.</li>
</ul>

## 如何实现导出功能?
<ul>
<li>1.GenerateServiceAndAction配置GENERATE_EXPORT为true,或者自己手动编写,参考SysUserController(以下以SysUser为例讲解实现).</li>
<li>2.配置phone-admin项目下src/main/resources/properties/excelExport.properties文件,key为SysUser,value为json,导出字段的对应中文.</li>
<li>3.实现phone-core项目的com.ly.base.core.excel.export.ext.SysUserExcelExport,注释见父类ExcelExportSuper.</li>
<li>方法说明:</li>
<li>formatValue:格式化非直接展示值的数据,如性别,DB中存储的为0或1,将其替换为女或男.t为当前对象,key为当前field.<br/>例如:switch (key) {case "sex":return "0".equals(t.getSex()) ? WOMAN :MAN;}</li>
<li>formatCondition:格式化查询条件,将查询条件展示到excel标题上.<br/>例如:StringBuffer sb = new StringBuffer();if (t.getName()!=null) {sb.append("姓名:"+t.getName()+BANK)}return sb.toString();.</li>
<li>getReportName:excel的文件名,如需修改请修改父类.<br/>例如:return "用户列表";则导出文件名为:用户列表2017-07-05.</li>
<li>更多方法配置请重写父类方法,方法说明请查看父类注释.</li>
</ul>

# 特别提醒
<ul>
<li>数据验证请在Proxy中完善checkData方法</li>
<li>条件查询请在Proxy中完善getConditions方法,使用条件查询务必在SysUserMapper.xml中复制一份selectExtend的sql,记得修改表名哦</li>
</ul>

QQ群:
<ul>
<li>深圳Java程序员 199267671</li>
<li>深圳Java(自学交流) 99281683</li>
</ul>
