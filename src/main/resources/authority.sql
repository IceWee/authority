/*
    __  ___      _____ ____    __ 
   /  |/  /_  __/ ___// __ \  / / 
  / /|_/ / / / /\__ \/ / / / / /  
 / /  / / /_/ /___/ / /_/ / / /___
/_/  /_/\__, //____/\___\_\/_____/
       /____/                 
    
### Create database
# -------------------------------------
# Database Type		: MySQL
# Version		 	: 50716
# Host/Port			: localhost:3306
# Database Name   	: authority
# -------------------------------------
*/
CREATE DATABASE `authority` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- Spring security内置表，用于使用Remember-me功能，将cookie持久化使用
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='remember-me记录表';


-- 用户表
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `username` varchar(32) DEFAULT NULL COMMENT '登录名',
  `name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `hidden` int(255) DEFAULT NULL COMMENT '隐藏（是1否0）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- 系统管理员用户
INSERT INTO `authority`.`sys_user` (`id`, `username`, `name`, `password`, `mobile`, `status`, `hidden`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('1', 'admin', '超级管理员', '2630d0280856135d2c7a19aee22ce42b37a060a99f512801ddb5ff522b33efcb34a3be20435ace78', '13800013800', '0', 1, now(), 'admin', now(), 'admin');

-- 用户组表
CREATE TABLE `sys_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '用户组名',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户组';

-- 用户组关联表
CREATE TABLE `sys_user_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `user_id` int(11) DEFAULT NULL COMMENT '用户PK',
  `group_id` int(11) DEFAULT NULL COMMENT '用户组PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户组关联';

-- 角色表
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `code` varchar(32) DEFAULT NULL COMMENT '角色编码',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色';

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `user_id` int(11) DEFAULT NULL COMMENT '用户PK',
  `role_id` int(11) DEFAULT NULL COMMENT '角色PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色关联';

-- 资源表
CREATE TABLE `sys_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '资源名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级资源ID',
  `type` int(11) DEFAULT NULL COMMENT '资源类型（功能地址1，其他2）',
  `url` varchar(500) DEFAULT NULL COMMENT '资源地址',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='资源';

-- 菜单表
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级菜单ID',
  `resource_id` int(11) DEFAULT NULL COMMENT '资源ID',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- 权限表
CREATE TABLE `sys_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '权限名称',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='权限';

-- 资源权限关联表
CREATE TABLE `sys_resource_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `resource_id` int(11) DEFAULT NULL COMMENT '资源PK',
  `right_id` int(11) DEFAULT NULL COMMENT '权限PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='资源权限关联';

-- 角色权限关联表
CREATE TABLE `sys_role_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `role_id` int(11) DEFAULT NULL COMMENT '角色PK',
  `right_id` int(11) DEFAULT NULL COMMENT '权限PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色权限关联';

-- 用户组权限关联表
CREATE TABLE `sys_group_right` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `group_id` int(11) DEFAULT NULL COMMENT '用户组PK',
  `right_id` int(11) DEFAULT NULL COMMENT '权限PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户组权限关联';

