CREATE DATABASE `gpay`;
USE `gpay`;
drop table if exists mg_config;
create table mg_config
(
    id          bigint(20)   not null comment '主键',
    name        varchar(512) not null comment '名称',
    value       text comment '值',
    remark      text comment '备注',
    create_id   bigint(20)   not null comment '创建人',
    update_id   bigint(20) default null comment '更新人',
    create_time datetime     not null comment '创建时间',
    update_time datetime   default null comment '更新时间',
    primary key (id),
    index idx_name (name),
    index idx_create_time (create_time)
) engine = innodb
  default charset = utf8mb4 comment ='配置表';

drop table if exists mg_log_operation;
create table mg_log_operation
(
    id             bigint(20) not null comment '主键',
    operation      varchar(512) default null comment '用户操作',
    request_url    varchar(512) default null comment '请求地址',
    request_params text comment '请求参数',
    request_time   int(11)    not null comment '请求时长(毫秒)',
    user_agent     varchar(512) default null comment '用户代理',
    ip             varchar(64)  default null comment 'ip地址',
    status         tinyint(2) not null comment '状态 0失败 1成功',
    create_id      bigint(20)   default null comment '创建人',
    create_user    varchar(128) default null comment '用户名',
    create_time    datetime   not null comment '创建时间',
    primary key (id),
    index idx_create_time (create_time),
    index idx_operation (operation),
    index idx_create_user (create_user)
) engine = innodb
  default charset = utf8mb4 comment ='操作日志表';


drop table if exists mg_menu;
create table mg_menu
(
    id          bigint(20)              not null comment '主键',
    pid         bigint(20)    default 0 not null comment '父id',
    path        varchar(512)  default null comment '路由地址',
    component   varchar(512)            not null comment '组件',
    type        tinyint(4)              not null comment '类型 1菜单 2按钮',
    redirect    varchar(512)  default null comment '跳转地址',
    name        varchar(256)  default null comment 'the name is used by <keep-alive> (must set!!!)',
    always_show tinyint(1)    default 0 not null comment '只有一个子菜单时是否显示主菜单',
    permissions varchar(1024) default null comment '菜单权限',
    breadcrumb  tinyint(1)    default 1 not null comment '0隐藏面包屑 1不隐藏面包屑',
    active_menu varchar(512)  default null comment '如果设置，将高亮指定菜单',
    title       varchar(128)            null comment '标题',
    icon        varchar(128)            null comment '图标',
    sort        int(11)       default 0 not null comment '菜单排序',
    status      tinyint(1)    default 1 not null comment '状态 0停用 1启用',
    primary key (id),
    index idx_sort (sort)
) engine = innodb
  default charset = utf8mb4 comment ='菜单表';


drop table if exists mg_role;
create table mg_role
(
    id          bigint(20)   not null comment '主键',
    name        varchar(128) not null comment '角色名',
    remark      varchar(1024) default null comment '备注',
    create_id   bigint(20)   not null comment '创建人',
    update_id   bigint(20)    default null comment '更新人',
    create_time datetime     not null comment '创建时间',
    update_time datetime      default null comment '更新时间',
    primary key (id),
    unique uk_name (name)
) engine = innodb
  default charset = utf8mb4 comment ='角色表';


drop table if exists mg_role_menu;
create table mg_role_menu
(
    id          bigint(20) not null comment '主键',
    role_id     bigint(20) not null comment '角色id',
    menu_id     bigint(20) not null comment '菜单id',
    create_id   bigint(20) not null comment '创建人',
    create_time datetime   not null comment '创建时间',
    primary key (id),
    key idx_role_id (role_id),
    key idx_menu_id (menu_id)
) engine = innodb
  default charset = utf8mb4 comment ='角色菜单关联表';


drop table if exists mg_user;
create table mg_user
(
    id            bigint(20)              not null comment '主键',
    username      varchar(128)            not null comment '用户名',
    password      varchar(128)            not null comment '密码',
    gender        tinyint(2)  default null comment '性别 1男 2女 3未知',
    mobile        varchar(64) default null comment '手机号',
    super_manager tinyint(2)  default 0   not null comment '超级管理员 0不是 1是',
    status        tinyint(2)  default 1   not null comment '状态 0停用 1启用',
    create_id     bigint(20)              not null comment '创建人',
    update_id     bigint(20)  default null comment '更新人',
    create_time   datetime                not null comment '创建时间',
    update_time   datetime    default null comment '更新时间',
    rm_tag        tinyint(2)  default '0' not null comment '删除标记 0正常 1删除',
    primary key (id),
    unique uk_username (username),
    index idx_create_time (create_time)
) engine = innodb
  default charset = utf8mb4 comment ='系统用户';


drop table if exists mg_user_role;
create table mg_user_role
(
    id          bigint(20) not null comment '主键',
    user_id     bigint(20) not null comment '用户id',
    role_id     bigint(20) not null comment '角色id',
    create_id   bigint(20) not null comment '创建人',
    create_time datetime   not null comment '创建时间',
    primary key (id),
    index idx_role_id (role_id),
    index idx_user_id (user_id)
) engine = innodb
  default charset = utf8mb4 comment ='用户角色关联表';


drop table if exists mg_user_token;
create table mg_user_token
(
    id          bigint(20)   not null comment '主键',
    user_id     bigint(20)   not null comment '用户id',
    token       varchar(512) not null comment 'token',
    expire_time datetime     not null comment '过期时间',
    create_time datetime     not null comment '创建时间',
    primary key (id),
    unique uk_token (token)
) engine = innodb
  default charset = utf8mb4 comment ='用户token表';
