### What
一个社交网站

用户登录注册、消息通知

帖子发布、搜索、点赞、评论、关注

### 技术栈
JDK21、SpringCloud、MySQL、Redis、RocketMQ、Minio、Elasticsearch、Canal、Docker

### API接口地址
http://localhost:9080/doc.html

### 主要细节
* 使用knife4j、SpringDoc融合Spring Cloud Gateway输出整个项目的API文档
* 全局统一异常处理器处理DispatcherServlet中的异常，自定义ErrorController来处理其它异常情况，如404、过滤器中的异常
  达到全局统一
* 实现ResponseBodyAdvice接口，对返回结果进行统一封装。
* 网关统一认证、黑名单处理、接口限流。
* 使用Canal实现MySQL的增量订阅，监听帖子发布数据，投递到RocketMQ中，同步到Elasticsearch中，用于帖子检索。
* 使用Minio实现对象存储，用于存储帖子的配图。
* 使用RocketMQ实现系统的消息通知，如点赞、评论、关注、系统通知。