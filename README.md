# 安能达OA

## 介绍

安能达OA为安能达工程公司办公提供了完善的管理方案。它从组织架构、员工、流程等多个功能模块，支撑管理每一个节点的进程与数据。它让整个办公流程井然有序，不仅帮助管理者高效协作和提升生产效率，另外对于管理者而言数据透明公开，随时随地知晓员工考勤状态、每日工作情况和提交的流程进度。

安能达OA包括以下功能模块，用户、认证、工作流、项目管理、文件管理等、有效地进行配，并采用了先进的软件技术架构（Spring Boot/Cloud、Vue前后台分离，界面使用多种VSCode主题风格）。

### 技术框架

后端技术框架:

- 核心框架：Spring Boot
- 持久层框架: Mybatis-plus
- 缓存存储：Redis
- 服务发现：Nacos
- 日志管理：Logback
- 项目管理框架: Maven

前端技术框架:

- 前端框架：vue.js
- 路由：vue-router
- 状态管理：vuex
- 国际化：vue-i18n
- 数据交互：axios
- UI框架：element-ui, view-design

### 开发说明

安能达OA使用的是微服务架构模式，因此在项目运行中，存在一部分功能需要依赖其它系统提供的微服务能力：

需要启动oa-gateway（网关）、oa-module-bpm（流程工作流）、oa-module-system（系统基础数据）等模块服务，访问时通过请求网关进行服务转发。

### 服务部署

服务启动前，需要先准备好相应启动环境，mysql、redis、nacos、xxl-job，并将对应的ip地址、账号密码配置到nacos的配置中心

[nacos配置文件](http://47.109.89.50:8848/nacos/index.html#/configurationManagement?dataId=&group=&appName=&namespace=ea6a3182-ca27-4593-8acd-7d86f7284837&pageSize=&pageNo=&namespaceShowName=pro)
