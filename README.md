# Art: Your Open-Source AI Application Development Platform for the Java Ecosystem

<a target="_blank" href="">
  <img alt="Star" src="https://gitee.com/fxz-cloud/art/badge/star.svg?theme=dark">
</a>
<a target="_blank" href="">
  <img alt="Fork" src="https://gitee.com/fxz-cloud/art/badge/fork.svg?theme=dark">
</a>
<a target="_blank" href="">
  <img alt="Spring Boot " src="https://img.shields.io/static/v1?label=Spring Boot &message=3.0.6&color=blue">
</a>
<a target="_blank" href="">
  <img alt="Spring Cloud" src="https://img.shields.io/static/v1?label=Spring Cloud&message=2022.0.2 &color=blue">
</a>
<a target="_blank" href="">
  <img alt="Spring Cloud Alibaba" src="https://img.shields.io/static/v1?label=Spring Cloud Alibaba &message=2022.0.0.0&color=blue">
</a>
<a target="_blank" href="">
  <img alt="OAuth 2.1" src="https://img.shields.io/static/v1?label=OAuth 2.1&message=0.4.2&color=blue">
</a>
<a target="_blank" href="">
  <img alt="JDK" src="https://img.shields.io/badge/JDK-17-blue.svg"/>
</a>
<a target="_blank" href="">
<img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-%20"/>
</a>
<br/>

---

## 🚀 项目简介

Art 是一个开源的、一站式 AI 应用开发平台，其灵感来源于行业领先的 [Coze](https://www.coze.com/) 和 [Dify](https://dify.ai/)。我们致力于将这些先进的 LLM 应用编排理念带入 Java 世界，为广大 Java 开发者提供一个熟悉、高效、稳定且易于扩展的 AI 应用构建环境。

与 Coze 和 Dify 不同，Art 完全基于 Java 技术栈构建。我们的目标是让企业和开发者能够利用成熟的 Spring 生态，快速构建、部署和管理功能强大的、生产级别的 AI 应用，特别是那些需要复杂逻辑编排和企业级集成的场景。

## ✨ 核心特性与AI能力

*   **可视化 AI 工作流 (AI Workflow)**: 通过拖拽式的画布，将语言模型（LLMs）、工具集（Plugins）、代码块、知识库等原子能力，编排为功能强大的 AI 应用。无论是构建一个简单的智能问答机器人，还是一个复杂的多 Agent 协作系统，都能轻松实现。
*   **深度知识整合与检索 (RAG)**: Art 的目标是构建一个先进的 RAG 引擎。不仅支持传统的向量检索，我们未来的核心能力将是实现 **知识图谱与向量数据库的混合检索**。这种方式能让模型在回答问题时，既能利用向量匹配的广泛性，又能借助知识图谱的精确逻辑关系，从而获得更深层次、更具推理能力的洞察。
*   **为 Java 而生**: 完全拥抱 Spring Boot 和 Spring Cloud 生态。Java 开发者可以无缝集成现有系统，并利用丰富的 Java 库来创建自定义工具（插件），将企业内部的任意服务或数据源封装成 AI 工作流的一个节点。
*   **模型无关与灵活扩展**: 支持接入并统一管理多种模型服务商（如 OpenAI, Azure, 文心一言, 通义千问等）。项目采用高度模块化的架构设计，无论是核心功能还是工具插件，都易于进行二次开发和功能扩展。

## 🎯 项目状态与路线图 (Roadmap)

我们正处于快速迭代的阶段，致力于实现一个功能完备的 AI 应用开发平台。

#### ✅ 已完成

*   **AI 工作流引擎**: 核心的 AI 工作流编排与执行引擎已经开发完成，支持基本的功能节点和流程控制。

#### 🚧 进行中 & 未来计划

*   **知识库 (Knowledge Base)**: 实现对多种数据源（PDF, Word, TXT, 网站, 数据库）的自动化接入、清洗、分片、向量化和存储。
*   **高级 RAG 引擎 (Advanced RAG Engine)**: 在现有基础上，正式落地 **知识图谱与向量数据库的混合检索** 方案，并持续优化检索效率与精准度。
*   **知识图谱构建与应用 (Knowledge Graph)**: 提供从非结构化和结构化数据中抽取知识、构建知识图谱的能力，并将其作为 RAG 的核心组件，赋能更强的逻辑推理和关联分析。
*   **Agent 智能体**: 增强 Agent 的自主任务规划、动态工具调用和多 Agent 协作能力。
*   **更丰富的插件生态**: 提供更多开箱即用的官方插件（如搜索、图表生成、API调用等），并建立开发者社区，鼓励贡献第三方插件。

## 🏗️ 项目模块说明

```
art
├── _other                  -- SQL脚本、Docker配置等
├── art-api                 -- 各模块API接口定义 (DTOs, Feign clients)
├── art-auth                -- 统一认证中心
├── art-framework           -- 核心基础框架 (封装了各种通用组件)
│   ├── art-base-core       -- 核心依赖与工具类
│   ├── art-framework-cache -- 缓存组件
│   ├── art-framework-mybatis -- ORM与数据权限组件
│   └── ...                 -- 其他框架组件
├── art-gateway             -- API网关
├── art-server              -- 各业务逻辑服务端
│   ├── art-ai              -- 【新增】AI核心业务模块
│   ├── art-server-system   -- 系统管理模块
│   └── ...                 -- 其他业务模块
└── pom.xml                 -- 主Maven配置文件
```

## 🤝 如何贡献

我们热烈欢迎任何形式的贡献！你可以通过以下方式参与项目：

1.  **发现和报告 Bug**: 提交 [Issues](https://github.com/your-github-username/art/issues)。
2.  **提交代码**: Fork 项目，完成你的功能开发或 Bug 修复后，提交 Pull Request。
3.  **完善文档**: 帮助我们改进文档，让项目更容易被理解和使用。

## 🍺加入我们
<table>
    <tr>
      <td><img src="https://cdn.jsdelivr.net/gh/fxzbiz/img@url/2022/11/19/O69mHa.png" width="120"/></td>
    </tr>
</table>

## 📄 开源许可

本项目遵循 [Apache 2.0](LICENSE) 开源许可协议。