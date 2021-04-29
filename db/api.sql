-- user table
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` VARCHAR(128) NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码',
  `mobile` VARCHAR(16) NOT NULL COMMENT '手机号',
  `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '用户状态 0停用 1启用',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '创建时间',
  `rm_tag` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '删除标记 0正常 1删除',
  PRIMARY KEY (`id`),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';

CREATE TABLE `t_user_token` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `token` VARCHAR(512) NOT NULL COMMENT 'token',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX (user_id),
  UNIQUE KEY token (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户token表';