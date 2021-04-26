CREATE DATABASE `gpay`;
USE `gpay`;

DROP TABLE IF EXISTS `mg_log_operation`;
CREATE TABLE `mg_log_operation`
(
    `id`             bigint(16) NOT NULL COMMENT '主键',
    `operation`      varchar(512) DEFAULT NULL COMMENT '用户操作',
    `request_url`    varchar(512) DEFAULT NULL COMMENT '请求地址',
    `request_params` text COMMENT '请求参数',
    `request_time`   int(11)    NOT NULL COMMENT '请求时长(毫秒)',
    `user_agent`     varchar(512) DEFAULT NULL COMMENT '用户代理',
    `ip`             varchar(64)  DEFAULT NULL COMMENT 'ip地址',
    `status`         tinyint(2) NOT NULL COMMENT '状态 0失败 1成功',
    `create_id`      bigint(16)   DEFAULT NULL COMMENT '创建人',
    `create_user`    varchar(128) DEFAULT NULL COMMENT '用户名',
    `create_time`    datetime   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_create_time` (`create_time`),
    INDEX idx_operation (operation),
    INDEX idx_create_user (create_user)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

DROP TABLE IF EXISTS `mg_menu`;
CREATE TABLE `mg_menu`
(
    `id`          bigint(16)    NOT NULL COMMENT '主键',
    `pid`         bigint(16)    NOT NULL DEFAULT '0' COMMENT '父id',
    `path`        varchar(512)  NOT NULL DEFAULT '' COMMENT '路由地址',
    `component`   varchar(512)  NOT NULL COMMENT '组件',
    `type`        tinyint(4)    NOT NULL COMMENT '类型 1菜单 2按钮',
    `hidden`      tinyint(1)    NOT NULL DEFAULT '0' COMMENT '隐藏 0不隐藏 1隐藏',
    `redirect`    varchar(512)  NOT NULL DEFAULT '' COMMENT '跳转地址',
    `name`        varchar(256)  NOT NULL DEFAULT '' COMMENT 'the name is used by <keep-alive> (must set!!!)',
    `always_show` tinyint(1)    NOT NULL DEFAULT '0' COMMENT '只有一个子菜单时是否显示主菜单',
    `permissions` varchar(1024) NOT NULL DEFAULT '' COMMENT '菜单权限',
    `breadcrumb`  tinyint(1)    NOT NULL DEFAULT '1' COMMENT '0隐藏面包屑 1不隐藏面包屑',
    `active_menu` varchar(512)  NOT NULL DEFAULT '' COMMENT '如果设置，将高亮指定菜单',
    `title`       varchar(128)  NOT NULL COMMENT '标题',
    `icon`        varchar(128)  NOT NULL COMMENT '图标',
    `sort`        int(11)       NOT NULL DEFAULT '0' COMMENT '菜单排序',
    `status`      tinyint(1)    NOT NULL DEFAULT '1' COMMENT '状态 0停用 1启用',
    PRIMARY KEY (`id`),
    INDEX `idx_sort` (`sort`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单表';

insert into `mg_menu`(`id`, `pid`, `path`, `component`, `type`, `hidden`, `redirect`, `name`, `always_show`,
                      `permissions`, `breadcrumb`, `active_menu`, `title`, `icon`, `sort`, `status`)
values (1, 0, '/system', 'Layout', 1, 0, '', '系统功能', 1, '', 1, '', '系统功能', 'system', 0, 1),
       (2, 1, 'menu', 'menu/index', 1, 0, '', '菜单管理', 0, '', 1, '', '菜单管理', 'menu', 2, 1),
       (1207275475953606658, 1, 'user', 'user/index', 1, 0, '', '用户管理', 0, 'mg:role:list,mg:role:getByUser', 1, '',
        '用户管理', 'user', 1, 1),
       (1207276248561152001, 1, 'role', 'role/index', 1, 0, '', '角色管理', 0, '', 1, '', '角色管理', 'role', 3, 1),
       (1207276483303763969, 1, 'log', 'log/index', 1, 0, '', '日志', 0, 'mg:log:page', 1, '', '日志', 'log', 4, 1),
       (1207276926083854337, 2, '', 'Button', 2, 0, '', '新增根菜单', 0, 'mg:menu:rootadd,mg:menu:add', 1, '', '', '', 0, 1),
       (1207277652847685634, 0, 'http://localhost:9568/manage/swagger-ui.html', 'Layout', 1, 0, '', '接口文档', 0, '', 1,
        '', '接口文档', 'link', 1, 1),
       (1207312840671674369, 1207275475953606658, '', 'Button', 2, 0, '', '查询', 0, 'mg:user:page', 1, '', '', '', 0, 1),
       (1207312948930854913, 1207275475953606658, '', 'Button', 2, 0, '', '新增', 0, 'mg:user:add', 1, '', '', '', 0, 1),
       (1207313094955548673, 1207275475953606658, '', 'Button', 2, 0, '', '修改', 0, 'mg:user:edit', 1, '', '', '', 0, 1),
       (1207313165424050178, 1207275475953606658, '', 'Button', 2, 0, '', '删除', 0, 'mg:user:remove', 1, '', '', '', 0,
        1),
       (1207315365105811457, 1207276248561152001, '', 'Button', 2, 0, '', '查询', 0, 'mg:role:page', 1, '', '', '', 0, 1),
       (1207315486644158465, 1207276248561152001, '', 'Button', 2, 0, '', '新增', 0,
        'mg:role:add,mg:role:getMenus,mg:menu:getTree', 1, '', '', '', 0, 1),
       (1207315552452788226, 1207276248561152001, '', 'Button', 2, 0, '', '修改', 0,
        'mg:role:edit,mg:role:getMenus,mg:menu:getTree', 1, '', '', '', 0, 1),
       (1207315799031726081, 1207276248561152001, '', 'Button', 2, 0, '', '删除', 0, 'mg:role:remove', 1, '', '', '', 0,
        1),
       (1207474062448738306, 2, '', 'Button', 2, 0, '', '新增', 0, 'mg:menu:add', 1, '', '', '', 0, 1),
       (1207478281733939201, 2, '', 'Button', 2, 0, '', '修改', 0, 'mg:menu:edit', 1, '', '', '', 0, 1),
       (1207478488014004226, 2, '', 'Button', 2, 0, '', '删除', 0, 'mg:menu:remove', 1, '', '', '', 0, 1),
       (1207480932173357057, 1207276483303763969, '', 'Button', 2, 0, '', '查询', 0, 'mg:log:page', 1, '', '', '', 0, 1),
       (1207481715275079681, 2, '', 'Button', 2, 0, '', '查询', 0, 'mg:menu:getAll', 1, '', '', '', 0, 1);


DROP TABLE IF EXISTS `mg_role`;
CREATE TABLE `mg_role`
(
    `id`          bigint(16)   NOT NULL COMMENT '主键',
    `name`        varchar(128) NOT NULL COMMENT '角色名',
    `remark`      varchar(1024) DEFAULT NULL COMMENT '备注',
    `create_id`   bigint(16)   NOT NULL COMMENT '创建人',
    `update_id`   bigint(16)    DEFAULT NULL COMMENT '更新人',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

insert into `mg_role`(`id`, `name`, `remark`, `create_id`, `update_id`, `create_time`, `update_time`)
values (1207276640430780417, '一般管理员', '具备当前所有菜单', 1, NULL, '2019-12-18 20:29:16', NULL),
       (1207479083793915906, '只能登录', NULL, 1, NULL, '2019-12-19 09:53:42', NULL),
       (1207484254867431425, '客服', '只具备查看权限', 1, NULL, '2019-12-19 10:14:15', NULL);

DROP TABLE IF EXISTS `mg_role_menu`;
CREATE TABLE `mg_role_menu`
(
    `id`          bigint(16) NOT NULL COMMENT '主键',
    `role_id`     bigint(16) NOT NULL COMMENT '角色id',
    `menu_id`     bigint(16) NOT NULL COMMENT '菜单id',
    `create_id`   bigint(16) NOT NULL COMMENT '创建人',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色菜单关联表';

insert into `mg_role_menu`(`id`, `role_id`, `menu_id`, `create_id`, `create_time`)
values (1207479023077171201, 1207276640430780417, 1, 1, '2019-12-19 09:53:28'),
       (1207479023093948418, 1207276640430780417, 1207275475953606658, 1, '2019-12-19 09:53:28'),
       (1207479023093948419, 1207276640430780417, 2, 1, '2019-12-19 09:53:28'),
       (1207479023102337025, 1207276640430780417, 1207276248561152001, 1, '2019-12-19 09:53:28'),
       (1207479023102337026, 1207276640430780417, 1207276483303763969, 1, '2019-12-19 09:53:28'),
       (1207484254880014338, 1207484254867431425, 1207312840671674369, 1, '2019-12-19 10:14:15'),
       (1207484254888402945, 1207484254867431425, 1207481715275079681, 1, '2019-12-19 10:14:15'),
       (1207484254888402946, 1207484254867431425, 1207315365105811457, 1, '2019-12-19 10:14:15'),
       (1207484254896791553, 1207484254867431425, 1207276483303763969, 1, '2019-12-19 10:14:15'),
       (1207484254896791554, 1207484254867431425, 1207480932173357057, 1, '2019-12-19 10:14:15'),
       (1207484254896791555, 1207484254867431425, 1207277652847685634, 1, '2019-12-19 10:14:15'),
       (1207484254896791556, 1207484254867431425, 1, 1, '2019-12-19 10:14:15'),
       (1207484254905180161, 1207484254867431425, 1207275475953606658, 1, '2019-12-19 10:14:15'),
       (1207484254905180162, 1207484254867431425, 2, 1, '2019-12-19 10:14:15'),
       (1207484254905180163, 1207484254867431425, 1207276248561152001, 1, '2019-12-19 10:14:15');

DROP TABLE IF EXISTS `mg_user`;
CREATE TABLE `mg_user`
(
    `id`            bigint(16)   NOT NULL COMMENT '主键',
    `username`      varchar(128) NOT NULL COMMENT '用户名',
    `password`      varchar(128) NOT NULL COMMENT '密码',
    `gender`        tinyint(2)            DEFAULT NULL COMMENT '性别 1男 2女 3未知',
    `mobile`        varchar(64)           DEFAULT NULL COMMENT '手机号',
    `super_manager` tinyint(2)   NOT NULL DEFAULT '0' COMMENT '超级管理员 0不是 1是',
    `status`        tinyint(2)   NOT NULL DEFAULT '1' COMMENT '状态 0停用 1启用',
    `create_id`     bigint(16)   NOT NULL COMMENT '创建人',
    `update_id`     bigint(16)            DEFAULT NULL COMMENT '更新人',
    `create_time`   datetime     NOT NULL COMMENT '创建时间',
    `update_time`   datetime              DEFAULT NULL COMMENT '更新时间',
    `rm_tag`        tinyint(2)   NOT NULL DEFAULT '0' COMMENT '删除标记 0正常 1删除',
    PRIMARY KEY (`id`),
    UNIQUE `uk_username` (`username`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户';

insert into `mg_user`(`id`, `username`, `password`, `gender`, `mobile`, `super_manager`, `status`, `create_id`,
                      `update_id`, `create_time`, `update_time`, `rm_tag`)
values (1, 'admin', '$2a$10$NZp1NOTR/uR1Oh8IMVlyPemaueL8P.ZlcIvGNKdw7CZRnEfItjO/W', 1, '17332345631', 1, 1, 0, NULL,
        '2019-12-07 19:40:00', NULL, 0),
       (1207472422173884418, 'godfunc', '$2a$10$ByDrbRzojQkfGbYDbikQzuJ6BxWCUp88hyCzIoJL.Kny0YaL8Lk3K', 1,
        '17564345431', 0, 1, 1, 1, '2019-12-19 09:27:14', '2019-12-19 09:27:31', 0);


DROP TABLE IF EXISTS `mg_user_role`;
CREATE TABLE `mg_user_role`
(
    `id`          bigint(16) NOT NULL COMMENT '主键',
    `user_id`     bigint(16) NOT NULL COMMENT '用户id',
    `role_id`     bigint(16) NOT NULL COMMENT '角色id',
    `create_id`   bigint(16) NOT NULL COMMENT '创建人',
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

insert into `mg_user_role`(`id`, `user_id`, `role_id`, `create_id`, `create_time`)
values (1207484319900114945, 1207472422173884418, 1207484254867431425, 1, '2019-12-19 10:14:30');

DROP TABLE IF EXISTS `mg_user_token`;
CREATE TABLE `mg_user_token`
(
    `id`          bigint(16)   NOT NULL COMMENT '主键',
    `user_id`     bigint(16)   NOT NULL COMMENT '用户id',
    `token`       varchar(512) NOT NULL COMMENT 'token',
    `expire_time` datetime     NOT NULL COMMENT '过期时间',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE `uk_token` (`token`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户token表';