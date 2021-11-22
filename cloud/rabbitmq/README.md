## 安装
1. 安装`erlang`
2. 安装`socat`
3. 安装`rabbitmq-server`

### 1. 安装`erlang`
```shell
curl -s https://packagecloud.io/install/repositories/rabbitmq/erlang/script.rpm.sh | sudo bash
yum -y install erlang
```

### 2. 安装`socat`
```shell
yum -y install epel-release
yum -y install socat
```

### 3. 安装rabbitmq
首先去github[下载rpm安装包](https://github.com/rabbitmq/rabbitmq-server/releases) 。
以`3.9.10`版本为例(下面用的镜像地址下载的)

```shell
wget https://github.91chifun.workers.dev/https://github.com//rabbitmq/rabbitmq-server/releases/download/v3.9.10/rabbitmq-server-3.9.10-1.el7.noarch.rpm
rpm -ivh rabbitmq-server-3.9.10-1.el7.noarch.rpm
```

## 配置rabbitmq
1. 配置开机启动
2. 开启management
3. 设置admin用户和权限

### 1. 开机启动
```shell
systemctl enable rabbitmq-server
systemctl status rabbitmq-server start
```

### 2. 开启management
```shell
rabbitmq-plugins enable rabbitmq_management
```

### 3. 设置admin用户和权限
```shell
rabbitmqctl add_user admin admin
rabbitmqctl set_user_tags admin administrator
rabbitmqctl set_permissions -p / admin "." "." ".*"
systemctl restart rabbitmq-server
```

## 安装延时消息插件
到[github](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases) 下载安装包(**注意要和rabbitmq-server的版本号对应**，比如根据上面的版本号，我们需要安装3.9.X版本的`rabbitmq-delayed-message-exchange`)。

```shell
cd /usr/lib/rabbitmq/lib/rabbitmq_server-3.9.10/plugins
wget https://github.91chifun.workers.dev/https://github.com//rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.9.0/rabbitmq_delayed_message_exchange-3.9.0.ez
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
systemctl restart rabbitmq-server
```

### 登录到web管理界面
访问`http://ip:15672`，使用`admin/admin`登录。

在`exchanges`下点击`Add a new exchange`，选择Type可以看到`x-delayed-message`就证明延时消息插件已经安装好了。


## 创建高可用镜像队列集群
假设我们有三台机器，对应的hostname分别是`node1 node2 node3`

1. 在`node1`上执行以下命令，拷贝`.erlang.cookie`到`node2 node3`，
```shell
rabbitmqctl stop_app
scp /var/lib/rabbitmq/.erlang.cookie node2:/var/lib/rabbitmq/
scp /var/lib/rabbitmq/.erlang.cookie node3:/var/lib/rabbitmq/
```

2. 在`node1 node2 node3` 上都执行以下命令
```shell
rabbitmq-server -detached
```

3. 启动`node1`节点
```shell
rabbitmqctl start_app
```

4. 将`node2 node3` 加入到集群，分别在`node2 node3`上执行一下命令
```shell
rabbitmqctl join_cluster rabbit@node1
```

5. (可选)在`node1`上执行，修改集群的名称
```shell
rabbitmqctl set_cluster_name gpay_rabbitmq_cluster
```

6. (可选)在`node1`上执行，移除集群中的节点
```shell
rabbitmqctl forget_cluster_node rabbit@node3
```

7. 设置为镜像队列
```shell
rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}' 
```