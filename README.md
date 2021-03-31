## 系统说明

- 基于 Spring Cloud Alibaba 、Spring Boot 2.3.6、 Mybatis plus 的 **教育类系统**

### 核心依赖

| 依赖                   | 版本          |
| ---------------------- | ------------- |
| Spring Boot            | 2.3.6.RELEASE |
| Spring Cloud           | Hoxton.SR9    |
| Spring Cloud Alibaba   | 2.2.3.RELEASE |
| Mybatis Plus           | 3.4.1         |
| hutool                 | 5.5.1         |

### 模块说明

```
edu-uyeek
└── edu-common -- 系统公共模块
     ├── edu-common-core -- 公共工具类核心包
     ├── edu-common-datasource -- 动态数据源包
     ├── edu-common-mybatis -- mybatis 扩展封装
     ├── edu-common-security -- 安全工具类
     ├── edu-common-swagger -- 接口文档
├── edu-gateway -- Spring Cloud Gateway网关[9999]
├── edu-infrastructure -- 系统基础骨架
     ├── edu-nacos -- 服务注册中心和配置中心
     ├── edu-sentinel -- 服务限流、熔断、降级服务
└── edu-service -- 服务公共模块
     └── edu-service-classcard -- 电子班牌服务
     └── edu-service-course-- 课程服务
```