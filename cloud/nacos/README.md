## 快速开始
1. 检查`application.properties`中的配置，例如数据库连接信息、nacos授权相关配置
2. 检查`cluster.conf`中的集群地址是否一致
3. 如果使用的是mysql，在指定的的数据库中(例如：nacos)执行`nacos-mysql.sql`

## 获取nacos的jar，存放到target目录下

## JVM参数调整
调整`startup.sh`(linux)中的`-Xms`(堆最小空间)、`-Xmx`(堆最大空间)、`-Xmn`(堆中新生代空间)，如果当前开发环境的机器内存不足时，可以将值调小。