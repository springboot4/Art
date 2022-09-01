##  🚀项目介绍
[![star](https://gitee.com/fxzcloud/fxz-cloud/badge/star.svg?theme=dark)](https://gitee.com/fxzcloud/fxz-cloud/stargazers)
[![fork](https://gitee.com/fxzcloud/fxz-cloud/badge/fork.svg?theme=gray)](https://gitee.com/fxzcloud/fxz-cloud/members)
<br/>
Not Only Scaffolding

全端代码开源，致力于打造成一款具有影响力的微服务开发平台


## 📖文档

🪜项目整体结构
```
fxz-cloud 
    ├── fxz-auth -- 认证服务器
    ├── fxz-common -- 通用工具封装
       ├── fxz-common-canal -- 针对canal封装
       ├── fxz-common-core -- web基础配置
       ├── fxz-common-data-permission 数据权限封装
       ├── fxz-common-database 多数据源
       ├── fxz-common-dependencies -- 依赖管理
       ├── fxz-common-es es配置
       ├── fxz-common-file 文件管理模块
       ├── fxz-common-gateway 动态网关
       ├── fxz-common-idempotency -- 幂等控制组件
       ├── fxz-common-jackson -- Json序列化配置
       ├── fxz-common-log -- 日志配置
       ├── fxz-common-mp -- mybatis-plus配置
       ├── fxz-common-mq -- 消息队列封装
          ├── fxz-common-mq-rabbit -- rabbitMq消息队列
          ├── fxz-common-mq-redis -- redis轻量级消息队列
       ├── fxz-common-redis -- 多级缓存、分布式锁模块
       ├── fxz-common-seata -- 分布式事务
       ├── fxz-common-security -- 安全模块
       ├── fxz-common-sequence -- 发号器（序列号生成器）
       ├── fxz-common-sms -- 阿里云短信服务
       ├── fxz-common-websocket  -- websocket
       ├── fxz-common-xxl-job  -- xxl-job配置
    ├── fxz-gateway -- 网关模块
    ├── fxz-generate -- 代码生成器
    ├── fxz-scheduled -- quartz定时任务模块
    ├── fxz-system -- 系统服务
       ├── fxz-system-api -- feign接口
       ├── fxz-system-biz -- 业务模块
    ├── fxz-z-laboratory -- 实验性模块
    ├──_other -- 项目相关文件
       ├── db -- 数据库相关文件
       ├── docker -- docker环境
       ├── es -- es索引信息   
       ├── middleware -- 一些本地启动的中间件
       ├── ui -- 前端代码
          ├── admin-vue2 -- 管理员界面
          ├── mall-uni   -- 商城小程序界面
```
[🔥🔥🔥快速启动](https://fxzcloud.gitee.io/docs/)


## 🪞基础功能
数据权限，角色支持多种数据权限设定，支持自定义进行拓展。
![](https://minio.pigx.vip/oss/2022/09/hYx7vs.png)
动态网关，网关支持通过前端动态配置路由，不需要在nacos中维护路由信息，修改自动加载，无需重启服务。
![](https://minio.pigx.vip/oss/2022/09/ynUvTt.png)
动态数据源&代码生成,支持动态添加数据源与项目基础crud代码生成。
![](https://minio.pigx.vip/oss/2022/09/rComtz.png)
![](https://minio.pigx.vip/oss/2022/09/BVZE8h.png)
动态路由
![](https://minio.pigx.vip/oss/2022/09/bCzQSv.png)
多种登录方式
![](https://minio.pigx.vip/oss/2022/09/4eoo2d.png)
字典管理
![](https://minio.pigx.vip/oss/2022/09/h6y7X8.png)
强退用户
![](https://minio.pigx.vip/oss/2022/09/RffesX.png)
审计日志
![](https://minio.pigx.vip/oss/2022/09/LOKhRX.png)
## 🎁商城管理
秒杀活动
![](https://minio.pigx.vip/oss/2022/09/qAFJUh.png)
![](https://minio.pigx.vip/oss/2022/09/89RNFg.png)
优惠券活动
![](https://minio.pigx.vip/oss/2022/09/9AnfQ2.png)
![](https://minio.pigx.vip/oss/2022/09/GrC1kg.png)
![](https://minio.pigx.vip/oss/2022/09/tpMtYV.png)
商品检索
![](https://minio.pigx.vip/oss/2022/09/1nPvz6.png)
订单管理
![](https://minio.pigx.vip/oss/2022/09/0xju53.png)
会员管理
![](https://minio.pigx.vip/oss/2022/09/4AA7JQ.png)

## 📱移动端展示
支持多种登录方式
![](https://minio.pigx.vip/oss/2022/09/7POcE0.png)
![](https://minio.pigx.vip/oss/2022/09/I86DRb.png)
![](https://minio.pigx.vip/oss/2022/09/I28W9e.png)
![](https://minio.pigx.vip/oss/2022/09/QPGuiC.png)
![](https://minio.pigx.vip/oss/2022/09/DswHAG.png)
![](https://minio.pigx.vip/oss/2022/09/y04bpB.png)
## 说明
脚手架与商城为两个单独项目，功能可按需引入。如仅需脚手架功能，可轻松去除。<br/>
低耦合，秒上手，稳得很！
## 其他功能
过于先进，不便展示。欢迎下载源码研究🧐欢迎star
## 🚀🚀🚀加入我们
qq群:932249645
<p>
<img src="https://minio.pigx.vip/oss/2022/07/FcAxsd.jpg" width = "330" height = "500"/>
</p>
