/*
Navicat MySQL Data Transfer

Source Server         : huwenhu
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : auth

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-08-08 18:12:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(200) DEFAULT NULL COMMENT '访问url',
  `message` varchar(50) DEFAULT NULL COMMENT '资源表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_resource_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource_role`;
CREATE TABLE `sys_resource_role` (
  `lId` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lRoleId` bigint(32) DEFAULT NULL,
  `lResourceId` bigint(32) DEFAULT NULL,
  PRIMARY KEY (`lId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nameZh` varchar(11) NOT NULL DEFAULT '' COMMENT '角色名称：管理员；用户',
  `name` varchar(11) DEFAULT NULL COMMENT '角色：ROLE_ADMIN;ROLE_USER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(11) NOT NULL DEFAULT '',
  `nickname` varchar(11) NOT NULL DEFAULT '',
  `password` varchar(64) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
