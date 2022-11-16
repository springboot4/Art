![logo](https://minio.pigx.vip/oss/2022/08/T4LHAz.svg)
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
    ├── art-system -- 系统服务
       ├── art-system -- 系统管理模块
       ├── art-generate -- 代码生成器模块
       ├── art-scheduled -- quartz定时任务模块
       ├── art-z-demos -- 实验性模块
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
<img src="https://minio.pigx.vip/oss/2022/07/FcAxsd.jpg" width = "330" height = "500"/>
</p>
微信交流群:
<p>
<img src="https://minio.pigx.vip/oss/2022/11/sGA58Z.jpg" width = "330" height = "500"/>
</p>
二维码过期可加本人微信:
<p>
<img src="https://minio.pigx.vip/oss/2022/10/HPJ944.jpg" width = "330" height = "500"/>
</p>

## 🍬说明文档
[🍓🍓🍓配套文档 fxzcloud.gitee.io/docs](https://fxzcloud.gitee.io/docs/)


## 💻系统应用
RBAC&数据权限，角色支持多种数据权限设定，支持自定义进行拓展。
![](https://minio.pigx.vip/oss/2022/09/2f0YJk.png)
SaaS多租户，自定义租户套餐.
![](https://minio.pigx.vip/oss/2022/10/vpuDaz.png)
![](https://minio.pigx.vip/oss/2022/10/TivOOd.png)
![](https://minio.pigx.vip/oss/2022/10/OmxgtF.png)
动态网关，网关支持通过前端动态配置，不需要在nacos中维护路由信息，修改自动加载，无需重启服务。
![](https://minio.pigx.vip/oss/2022/09/PxMBNC.png)

动态数据源&代码生成,支持动态添加数据源与项目基础crud代码生成。
![](https://minio.pigx.vip/oss/2022/09/LpRdJs.png)
![](https://minio.pigx.vip/oss/2022/09/aMfUqv.png)
支持多种登录方式,可自定义进行拓展。
![](https://minio.pigx.vip/oss/2022/09/76RYJu.png)
![](https://minio.pigx.vip/oss/2022/10/DseL2p.png)字典管理
![](https://minio.pigx.vip/oss/2022/09/sj2R4y.png)强退用户
![](https://minio.pigx.vip/oss/2022/09/FKsSGc.png)审计日志

## ![](https://minio.pigx.vip/oss/2022/09/kBIASc.png)🎁商城应用

秒杀活动
![](https://minio.pigx.vip/oss/2022/09/qAFJUh.png)
![](https://minio.pigx.vip/oss/2022/09/89RNFg.png)
优惠券活动
![](https://minio.pigx.vip/oss/2022/09/bHjYjU.png)
![](https://minio.pigx.vip/oss/2022/09/bGSyKP.png)
![](https://minio.pigx.vip/oss/2022/09/tpMtYV.png)
商品检索

![](https://minio.pigx.vip/oss/2022/09/BSJsJU.png)订单管理

![](https://minio.pigx.vip/oss/2022/09/d0EG2h.png)会员管理

## ![](https://minio.pigx.vip/oss/2022/09/S7DRyz.png)
📱移动端展示

支持多种登录方式
![](https://minio.pigx.vip/oss/2022/09/7POcE0.png)
![](https://minio.pigx.vip/oss/2022/09/I86DRb.png)
![](https://minio.pigx.vip/oss/2022/09/I28W9e.png)
![](https://minio.pigx.vip/oss/2022/09/QPGuiC.png)
![](https://minio.pigx.vip/oss/2022/09/DswHAG.png)
![](https://minio.pigx.vip/oss/2022/09/y04bpB.png)
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
| hutool                 | 5.8.6      |