<p align="center">
  <a href="https://idea360.cn">
   <img alt="idea360-Logo" src="https://raw.githubusercontent.com/qidian360/oss/master/images/idea360.cn.png">
  </a>
</p>

<p align="center">
  为简化开发工作、提高生产率而生
</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:cn.idea360%20a:log-kafka-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/cn.idea360/log-kafka-spring-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://github.com/qidian360/log-kafka-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/github/forks/qidian360/log-kafka-spring-boot-starter">
  </a>

  <a href="https://github.com/qidian360/log-kafka-spring-boot-starter">
    <img alt="maven" src="https://img.shields.io/github/stars/qidian360/log-kafka-spring-boot-starter">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>

  <a href="https://idea360.cn">
    <img alt="code style" src="https://img.shields.io/badge/%E5%BD%93%E6%88%91%E9%81%87%E4%B8%8A%E4%BD%A0-idea360.cn-brightgreen">
  </a>
</p>

## 它能够解决什么问题

帮助 `SpringBoot` 快速集成 `ELK`, 将日志生成到 `kafka`。支持的日志框架:

- logback
- log4j2


## 快速使用

#### 1. maven依赖添加SDK依赖

```xml
<dependency>
  <groupId>cn.idea360</groupId>
  <artifactId>log-kafka-spring-boot-starter</artifactId>
  <version>x.x.x</version>
</dependency>
```

#### 2. application.properties配置kafka信息

```properties
log-kafka.brokers=127.0.0.1:9092
log-kafka.topic=test
```

#### 3. logback使用

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```


#### 4. log4j2使用

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

## 联系我

* [Email](idea360@foxmail.com): idea360@foxmail.com
* [Blog](https://idea360.cn): idea360.cn

---
欢迎大家关注微信公众号. 您的支持是我写作的最大动力

![当我遇上你](https://raw.githubusercontent.com/qidian360/oss/master/images/wechat-qr-code-300px.png "当我遇上你")
