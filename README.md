![logo](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/d9Qz42.svg)

##  🚀项目介绍
[![license](https://img.shields.io/badge/License-Apache%202.0-%20)](https://gitee.com/fxz-cloud/art/blob/master/LICENSE)
[![star](https://gitee.com/fxz-cloud/art/badge/star.svg?theme=dark)](https://gitee.com/fxz-cloud/art/stargazers) 
[![fork](https://gitee.com/fxz-cloud/art/badge/fork.svg?theme=dark)](https://gitee.com/fxz-cloud/art/members)
<br/>
Art 是 FCloud 项目中的一员。Art 全端代码开源，支持RBAC 动态权限、SaaS多租户系统。


## 🪜项目结构
```lua
   art
    ├── art-api -- 接口管理
    ├── art-auth -- 认证服务器
    ├── art-framework -- 通用工具封装
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
       ├── art-spring-boot-starter-mq-rabbit -- rabbitMq消息队列
       ├── art-spring-boot-starter-mq-redis -- redis轻量级消息队列
       ├── art-spring-boot-starter-quartz -- quartz定时任务
       ├── art-spring-boot-starter-redis -- 多级缓存、分布式锁模块
       ├── art-spring-boot-starter-seata -- 分布式事务
       ├── art-spring-boot-starter-security -- 安全模块
       ├── art-spring-boot-starter-sequence -- 发号器（序列号生成器）
       ├── art-spring-boot-starter-sms -- 阿里云短信服务
       ├── art-spring-boot-starter-tenant -- SaaS多租户支持
       ├── art-spring-boot-starter-websocket  -- websocket
       ├── art-spring-boot-starter-xss  -- xss过滤
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
## 📖分层结构

项目中使用的是阿里的规范（详细可看https://github.com/alibaba/p3c 这里面的[Java开发手册（黄山版）.pdf](https://github.com/alibaba/p3c/blob/master/Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C(%E9%BB%84%E5%B1%B1%E7%89%88).pdf)），同时使用插件进行规约扫描

我们先来看下规范当中的目录结构

![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/26/Ql0Dur.png)

- 开放 API 层：可直接封装 Service 接口暴露成 RPC 接口；通过 Web 封装成 http 接口；网关控制层等。
- 终端显示层：各个端的模板渲染并执行显示的层。
- Web 层：主要是对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等。
- Service 层：相对具体的业务逻辑服务层。
- Manager 层：通用业务处理层，它有如下特征：
    -  1） 对第三方平台封装的层，预处理返回结果及转化异常信息，适配上层接口。
    -  2） 对 Service 层通用能力的下沉，如缓存方案、中间件通用处理。
    -  3） 与 DAO 层交互，对多个 DAO 的组合复用。
- DAO 层：数据访问层，与底层 MySQL、Oracle、Hbase、OB 等进行数据交互。
- 第三方服务：包括其它部门 RPC 服务接口，基础平台，其它公司的 HTTP 接口，如淘宝开放平台、支 付宝付款服务、高德地图服务等。
- 外部数据接口：外部（应用）数据存储服务提供的接口，多见于数据迁移场景中。

## 💻系统应用

| RBAC&数据权限                     | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/26/v5m3e1.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/Wb1Xck.png) |
| --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 多租户                            | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/26/IQ7uvi.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/26/mPf6tH.png) |
| 动态网关&字典管理                 | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/ZOGHdk.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/OZSRwm.png) |
| 动态数据源&代码生成,              | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/UCiIcm.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/DR2mTD.png) |
| 支持多种登录方式,可自定义进行拓展 | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/6Mr28s.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/3Z3LIE.png) |
| 强退用户&审计日志                 | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/q49Fii.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/q49Fii.png) |


## 🎁商城应用

| 秒杀活动          | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/WbsMOF.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/xzQlSf.png) |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 优惠券活动        | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/eQpsqF.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/a2FHWN.png) |
| 商品检索          | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/9V94xp.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/CKYzR4.png) |
| 订单管理&会员管理 | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/Xw6k8N.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/feS6Ao.png) |



## 📱移动端展示

| 支持多种登录方式 | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/uod52k.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/miu152.png) |
| ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 商品详情         | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/cYEjX8.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/pwUEN0.png) |
| 购物车           | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/qip5o6.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/3sTPGv.png) |

## 🔍项目说明
脚手架与商城为两个单独项目，功能可按需引入。如仅需脚手架功能，可轻松去除。<br/>
低耦合，秒上手，稳得很！
## ❓其他功能
过于先进，不便展示。欢迎下载源码研究🧐欢迎star
## 🍓依赖版本

| 依赖                   | 版本         |
|----------------------|------------|
| Spring Boot          | 2.7.7      |
| Spring Cloud         | 2021.0.5   |
| Spring Cloud Alibaba | 2021.0.4.0 |
| Mybatis Plus         | 3.5.3.1    |
| Hutool               | 5.8.11     |



## 🍺加入我们
| 交流群                  | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/dqDn4c.png) | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2023/01/11/rZTN38.jpg) |
| ----------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 二维码过期可加本人微信: | ![](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/11/19/O69mHa.png) |                                                              |

 


## 🍬说明文档
[🍓🍓🍓配套文档 fxzcloud.gitee.io/docs](https://fxzcloud.gitee.io/docs/)

## 🤝鸣谢
感谢 [![jetbrains](https://cdn.staticaly.com/gh/fxzbiz/img@url/2022/12/01/DGnop3.png)](https://www.jetbrains.com/)提供的免费License