![logo](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/d9Qz42.svg)
##  🚀项目介绍
Art 是 FCloud 项目中的一员。Art 全端代码开源，支持RBAC 动态权限、SaaS多租户系统。致力于打造成一款具有影响力的微服务开发平台。
 

## 🪜项目结构
```lua
   art
    ├── art-api -- 接口管理
    ├── art-auth -- 认证服务器
    ├── art-framework -- 通用工具封装
       ├── art-spring-boot-starter-banner -- banner信息
       ├── art-spring-boot-starter-canal -- 针对canal封装
       ├── art-spring-boot-starter-captcha -- 验证码封装
       ├── art-spring-boot-starter-core -- web基础配置
       ├── art-spring-boot-starter-data-permission -- 数据权限封装
       ├── art-spring-boot-starter-database -- 多数据源
       ├── art-spring-boot-starter-dependencies -- 依赖管理
       ├── art-spring-boot-starter-doc -- 接口文档
       ├── art-spring-boot-starter-es -- es配置
       ├── art-spring-boot-starter-file -- 文件管理模块
       ├── art-spring-boot-starter-gateway -- 动态网关
       ├── art-spring-boot-starter-idempotency -- 幂等控制组件
       ├── art-spring-boot-starter-jackson -- Json序列化配置
       ├── art-spring-boot-starter-lock -- 分布式锁模块
       ├── art-spring-boot-starter-log -- 日志配置
       ├── art-spring-boot-starter-mp -- mybatis-plus配置
       ├── art-spring-boot-starter-mq -- 消息队列封装
          ├── art-spring-boot-starter-mq-rabbit -- rabbitMq消息队列
          ├── art-spring-boot-starter-mq-redis -- redis轻量级消息队列
       ├── art-spring-boot-starter-redis -- 多级缓存、分布式锁模块
       ├── art-spring-boot-starter-seata -- 分布式事务
       ├── art-spring-boot-starter-security -- 安全模块
       ├── art-spring-boot-starter-sequence -- 发号器（序列号生成器）
       ├── art-spring-boot-starter-sms -- 阿里云短信服务
       ├── art-spring-boot-starter-tenant -- SaaS多租户支持
       ├── art-spring-boot-starter-websocket  -- websocket
       ├── art-spring-boot-starter-xxl-job  -- xxl-job配置
    ├── art-gateway -- 网关模块
    ├── art-server -- 系统服务
       ├── art-server-system -- 系统管理模块
       ├── art-server-generate -- 代码生成器模块
       ├── art-server-scheduled -- quartz定时任务模块
       ├── art-server-z-demos -- 实验性模块
    ├──_other -- 项目相关文件
       ├── db -- 数据库相关文件
       ├── docker -- docker环境
       ├── es -- es索引信息   
       ├── middleware -- 一些本地启动的中间件
       ├── ui -- 前端代码
          ├── admin-vue2 -- 管理员界面
          ├── mall-uni   -- 商城小程序界面
```

## 🍺加入我们
qq交流群:932249645
<p>
<img src="https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/dqDn4c.png" width = "330" height = "500"/>
</p>
微信交流群:
<p>
<img src="https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/HmxY0q.png" width = "330" height = "500"/>
</p>
二维码过期可加本人微信:
<p>
<img src="https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/O69mHa.png" width = "330" height = "500"/>
</p>

## 🍬说明文档
[🍓🍓🍓配套文档 fxzcloud.gitee.io/docs](https://fxzcloud.gitee.io/docs/)


## 💻系统应用
RBAC&数据权限，角色支持多种数据权限设定，支持自定义进行拓展。
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/PUVhgr.png)<br/>
SaaS多租户，自定义租户套餐.<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/uEiiVg.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/YrMBP4.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/Wb1Xck.png)<br/>
动态网关，网关支持通过前端动态配置，不需要在nacos中维护路由信息，修改自动加载，无需重启服务。<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/ZOGHdk.png)<br/>

动态数据源&代码生成,支持动态添加数据源与项目基础crud代码生成。<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/UCiIcm.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/DR2mTD.png)<br/>
支持多种登录方式,可自定义进行拓展。<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/6Mr28s.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/3Z3LIE.png)<br/>
字典管理<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/OZSRwm.png)<br/>
强退用户<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/q49Fii.png)<br/>
审计日志<br/>

## ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/IcEOnf.png)<br/>🎁商城应用

<br/>秒杀活动<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/WbsMOF.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/xzQlSf.png)
<br/>优惠券活动<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/eQpsqF.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/a2FHWN.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/9V94xp.png)
<br/>商品检索<br/>

![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/CKYzR4.png)
<br/>订单管理<br/>

![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/Xw6k8N.png)
<br/>会员管理<br/>

![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/feS6Ao.png)
## 📱移动端展示

支持多种登录方式<br/>
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/uod52k.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/miu152.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/cYEjX8.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/pwUEN0.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/qip5o6.png)
![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/3sTPGv.png)
## 🔍说明
脚手架与商城为两个单独项目，功能可按需引入。如仅需脚手架功能，可轻松去除。<br/>
低耦合，秒上手，稳得很！
## ❓其他功能
过于先进，不便展示。欢迎下载源码研究🧐欢迎star
## 依赖版本

| 依赖                   | 版本         |
| ---------------------- |------------|
| Spring Boot            | 2.7.0      |
| Spring Cloud           | 2021.0.1   |
| Spring Cloud Alibaba   | 2021.0.1.0 |
| Mybatis Plus           | 3.5.1      |
| hutool                 | 5.8.9      |