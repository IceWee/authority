# authority(认证与授权管理平台)

### 项目启动
> 1 初始化数据库，执行authority.sql，建库、建表
> 2 启动redis，并根据实际情况修改application*.yml中redis配置

### 技术
Spring Boot/MyBatis/Spring Security/Redis/Bootstrap/Thymeleaf...eg.

### 数据库
MySQL

### eclipse配置lombok
> 下载lombok.jar，放置到eclipse根目录，与eclipse.ini在同一目录即可，在eclipse.inif中加入如下配置重启eclipse即可生效

```
-Xbootclasspath/a:lombok.jar
-javaagent:lombok.jar
```
