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


CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `username` varchar(32) DEFAULT NULL COMMENT '登录名',
  `name` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(256) DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '密码盐，用于密码加密',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` int(11) DEFAULT NULL COMMENT '账号状态（正常0，锁定1，删除2）',
  `create_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `authority`.`sys_user` (`id`, `username`, `name`, `password`, `salt`, `mobile`, `status`, `create_date`, `update_date`) VALUES ('1', 'admin', 'Admin', 'ceb4f32325eda6142bd65215f4c0f371', 'admin', '13800013800', '0', '2017-04-20 02:55:34', '2017-04-20 02:55:34');
