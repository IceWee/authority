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
CREATE DATABASE `authority` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

-- Spring security内置表，用于使用Remember-me功能，将cookie持久化使用
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='remember-me记录表';


-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `username` varchar(32) DEFAULT NULL COMMENT '登录名',
  `name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `hidden` int(11) DEFAULT NULL COMMENT '隐藏（是1否0）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- 系统管理员用户
INSERT INTO `sys_user` (`username`, `name`, `password`, `mobile`, `status`, `hidden`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('admin', '超级管理员', '2630d0280856135d2c7a19aee22ce42b37a060a99f512801ddb5ff522b33efcb34a3be20435ace78', '13800013800', 0, 1, now(), 'admin', now(), 'admin');


-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `code` varchar(32) DEFAULT NULL COMMENT '角色编码',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `hidden` int(11) DEFAULT NULL COMMENT '隐藏（是1否0）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色';

-- 系统管理员角色
INSERT INTO `sys_role` (`code`, `name`, `remark`, `status`, `hidden`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('admin', '系统管理员', '系统管理员', 0, 1, now(), 'admin', now(), 'admin');


-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
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

-- 系统管理员角色授权
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `status`, `create_date`, `create_user`, `update_date`, `update_user`) 
VALUES (
(select `id` from `sys_user` where `username` = 'admin'), 
(select `id` from `sys_role` where `code` = 'admin'),  
1, now(), 'admin', now(), 'admin');


-- 资源分类表
DROP TABLE IF EXISTS `sys_resource_category`;
CREATE TABLE `sys_resource_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '资源分类名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级分类ID',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='资源分类';

-- 资源分类初始化数据
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源分类', '0', '0', '资源分类', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('系统管理', (select t.id from (select id from sys_resource_category where parent_id = 0) t), '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('用户管理', (select t.id from (select id from sys_resource_category where name = '系统管理') t), '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('角色管理', (select t.id from (select id from sys_resource_category where name = '系统管理') t), '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源管理', (select t.id from (select id from sys_resource_category where name = '系统管理') t), '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('菜单管理', (select t.id from (select id from sys_resource_category where name = '系统管理') t), '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource_category` (`name`, `parent_id`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源分类管理', (select t.id from (select id from sys_resource_category where name = '系统管理') t), '0', '', now(), 'admin', now(), 'admin');


-- 资源表
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '资源名称',
  `category_id` int(11) DEFAULT NULL COMMENT '资源分类ID',
  `type` int(11) DEFAULT NULL COMMENT '资源类型（功能1，接口2，其他3）',
  `url` varchar(500) DEFAULT NULL COMMENT '资源地址',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='资源';

-- 资源数据初始化
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('用户列表', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('新增用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/add', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('编辑用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/edit', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('更新用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/update', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('删除用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '0', '/system/user/delete', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('用户列表数据', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '1', '/ajax/system/user/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存用户数据', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '1', '/ajax/system/user/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('锁定用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '1', '/ajax/system/user/lock', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('解锁用户', (select t.id from (select id from sys_resource_category where name = '用户管理') t), '1', '/ajax/system/user/unlock', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('角色列表', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('新增角色', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/add', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存角色', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('编辑角色', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/edit', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('更新角色', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/update', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('删除角色', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '0', '/system/role/delete', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('角色列表数据', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '1', '/ajax/system/role/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存角色数据', (select t.id from (select id from sys_resource_category where name = '角色管理') t), '1', '/ajax/system/role/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('菜单列表', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('新增菜单', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/add', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('编辑菜单', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/edit', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存菜单', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('更新菜单', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/update', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('删除菜单', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '0', '/system/menu/delete', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('菜单列表数据', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '1', '/ajax/system/menu/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('菜单树数据', (select t.id from (select id from sys_resource_category where name = '菜单管理') t), '1', '/ajax/system/menu/tree', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源列表', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('新增资源', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/add', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存资源', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('编辑资源', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/edit', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('更新资源', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/update', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('删除资源', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '0', '/system/resource/delete', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源列表数据', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '1', '/ajax/system/resource/list', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源树数据', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '1', '/ajax/system/resource/tree', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存资源数据', (select t.id from (select id from sys_resource_category where name = '资源管理') t), '1', '/ajax/system/resource/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源分类树数据', (select t.id from (select id from sys_resource_category where name = '资源分类管理') t), '1', '/ajax/system/category/tree', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('保存资源分类数据', (select t.id from (select id from sys_resource_category where name = '资源分类管理') t), '1', '/ajax/system/category/save', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('更新资源分类数据', (select t.id from (select id from sys_resource_category where name = '资源分类管理') t), '1', '/ajax/system/category/update', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_resource` (`name`, `category_id`, `type`, `url`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('删除资源分类数据', (select t.id from (select id from sys_resource_category where name = '资源分类管理') t), '1', '/ajax/system/category/delete', '0', '', now(), 'admin', now(), 'admin');


-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级菜单ID',
  `resource_id` int(11) DEFAULT NULL COMMENT '资源ID',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='菜单';

-- 菜单初始化数据
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('系统菜单', '0', NULL, '0', '0', '系统菜单', now(), 'admin', now(), 'admin');
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('系统管理', (select t.id from (select id from sys_menu where parent_id = 0) t), NULL, '1', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('用户管理', (select t.id from (select id from sys_menu where name = '系统管理') t), (select id from sys_resource where name = '用户列表'), '1', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('角色管理', (select t.id from (select id from sys_menu where name = '系统管理') t), (select id from sys_resource where name = '角色列表'), '2', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('资源管理', (select t.id from (select id from sys_menu where name = '系统管理') t), (select id from sys_resource where name = '资源列表'), '3', '0', '', now(), 'admin', now(), 'admin');
INSERT INTO `sys_menu` (`name`, `parent_id`, `resource_id`, `sort`, `status`, `remark`, `create_date`, `create_user`, `update_date`, `update_user`) VALUES ('菜单管理', (select t.id from (select id from sys_menu where name = '系统管理') t), (select id from sys_resource where name = '菜单列表'), '4', '0', '', now(), 'admin', now(), 'admin');


-- 角色资源关联表
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `role_id` int(11) DEFAULT NULL COMMENT '角色PK',
  `resource_id` int(11) DEFAULT NULL COMMENT '权限PK',
  `status` int(11) DEFAULT NULL COMMENT '状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_user` varchar(32) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色资源关联';

-- 角色资源关系数据初始化
INSERT INTO `sys_role_resource` (`resource_id`, `status`, `create_date`, `create_user`, `update_date`, `update_user`) 
SELECT `id`, '0', now(), 'admin', now(), 'admin' FROM `sys_resource`;

UPDATE `sys_role_resource` SET `role_id` = (SELECT id FROM sys_role WHERE code = 'admin');
