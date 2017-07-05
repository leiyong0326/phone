/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : phone_distribution

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2016-11-23 21:10:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `PK` int(11) NOT NULL,
  `CREATE_BY` varchar(40) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `IP` varchar(40) DEFAULT NULL,
  `COUNTRY` varchar(40) DEFAULT NULL,
  `PROVINCE` varchar(40) DEFAULT NULL,
  `CITY` varchar(100) DEFAULT NULL,
  `AREA` varchar(40) DEFAULT NULL,
  `ADSL` varchar(40) DEFAULT NULL,
  `FROM` char(1) DEFAULT NULL,
  `BROWSER` varchar(200) DEFAULT NULL,
  `OS` varchar(100) DEFAULT NULL,
  `CREATE_NAME` varchar(40) DEFAULT NULL,
  `LOGIN_NAME` varchar(40) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_operater
-- ----------------------------
DROP TABLE IF EXISTS `log_operater`;
CREATE TABLE `log_operater` (
  `PK` int(11) NOT NULL,
  `CREATE_BY` varchar(40) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `TYPE` char(1) DEFAULT NULL,
  `MODEL` varchar(40) DEFAULT NULL,
  `FUNC` varchar(40) DEFAULT NULL,
  `CONTENT` varchar(100) DEFAULT NULL,
  `METHOD` varchar(40) DEFAULT NULL,
  `TITLE` varchar(40) DEFAULT NULL,
  `FROM` char(1) DEFAULT NULL,
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_account`;
CREATE TABLE `sys_account` (
  `PK` int(11) NOT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `DSC` varchar(100) DEFAULT NULL,
  `RATIO` decimal(8,6) DEFAULT NULL,
  `SCORE` int(11) DEFAULT NULL,
  `RATIO_UP` decimal(8,6) DEFAULT NULL,
  `VALID` int(11) DEFAULT NULL COMMENT '0所有层级,1上级 分成,2上一级分成',
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_account
-- ----------------------------
INSERT INTO `sys_account` VALUES ('0', '食物链最顶端的老王', '请问需要经纪人吗?', '1.000000', '999999', '0.000000', '1');
INSERT INTO `sys_account` VALUES ('2', '精英骑士', '再次加官进爵,挑战高富帅,迎娶白富美,还差一点点', '0.700000', '500', '0.000000', '1');
INSERT INTO `sys_account` VALUES ('3', '小学生', 'what?我要统治的可不是三年级,是整个小学好吗!', '0.500000', '0', '0.000000', '1');
INSERT INTO `sys_account` VALUES ('5', '青铜骑手', '表现亮眼的我终于得到了老板的认可,也赢得了人生中第一头毛驴', '0.600000', '100', '0.000000', '1');
INSERT INTO `sys_account` VALUES ('9', '黄金铁骑', '别摸我,摸掉了你可赔不起!', '0.800000', '2000', '0.000000', '1');
INSERT INTO `sys_account` VALUES ('10', '钻石王老五', '白富美算啥?我只缺小情人', '0.900000', '10000', '0.000000', '1');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `PK` varchar(40) NOT NULL,
  `UP_PK` varchar(40) DEFAULT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `TEXT` varchar(40) DEFAULT NULL,
  `URL` varchar(100) DEFAULT NULL,
  `ICON` varchar(100) DEFAULT NULL,
  `SEQ` int(11) DEFAULT NULL,
  `MENUTYPE` char(1) DEFAULT NULL COMMENT ' 0-菜单导航 1- 按钮',
  `ENABLE` char(1) DEFAULT NULL,
  `DSC` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('01', null, 'system', '系统管理', '', 'globe', '1', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0101', '01', 'staff', '菜单管理', 'src/system/menu.html', '', '1', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0101del', '01', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0101edit', '01', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0101show', '01', null, '查看', null, null, '3', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0102', '01', 'staff', '会员管理', 'src/system/staff.html', '', '2', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0102del', '0102', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0102edit', '0102', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0102show', '0102', null, '查看', null, null, '3', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0103', '01', 'role', '角色管理', 'src/system/role.html', '', '3', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0103del', '0103', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0103edit', '0103', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0103show', '0103', null, '查看', null, null, '3', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0104', '01', 'level', '会员等级管理', 'src/system/level.html', '', '4', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0104del', '0104', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0104edit', '0104', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0104show', '0104', null, '查看', null, null, '3', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('02', '', 'log', '日志管理', '', 'paste', '2', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0201', '02', 'login', '登录日志', 'src/log/login.html', '', '1', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0201del', '0201', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0201edit', '0201', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0201show', '0201', null, '查看', null, null, '3', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0202', '02', 'operater', '操作日志', 'src/log/operater.html', '', '2', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('0202del', '0202', null, '删除', null, null, '2', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0202edit', '0202', null, '编辑', null, null, '1', '1', '1', null);
INSERT INTO `sys_menu` VALUES ('0202show', '0202', null, '查看', null, null, '3', '1', '1', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `PK` int(11) NOT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `DSC` varchar(100) DEFAULT NULL,
  `ENABLE` char(1) DEFAULT NULL,
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('0', 'admin', 'admin', '1');
INSERT INTO `sys_role` VALUES ('1', 'user', '普通用户', '1');
INSERT INTO `sys_role` VALUES ('12', '管理员', null, '1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `ROLE_PK` int(11) NOT NULL,
  `MENU_PK` varchar(40) NOT NULL,
  PRIMARY KEY (`ROLE_PK`,`MENU_PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('0', '01');
INSERT INTO `sys_role_menu` VALUES ('0', '0102');
INSERT INTO `sys_role_menu` VALUES ('0', '0102del');
INSERT INTO `sys_role_menu` VALUES ('0', '0102edit');
INSERT INTO `sys_role_menu` VALUES ('0', '0102show');
INSERT INTO `sys_role_menu` VALUES ('0', '0103');
INSERT INTO `sys_role_menu` VALUES ('0', '0103del');
INSERT INTO `sys_role_menu` VALUES ('0', '0103edit');
INSERT INTO `sys_role_menu` VALUES ('0', '0103show');
INSERT INTO `sys_role_menu` VALUES ('0', '0104');
INSERT INTO `sys_role_menu` VALUES ('0', '0104del');
INSERT INTO `sys_role_menu` VALUES ('0', '0104edit');
INSERT INTO `sys_role_menu` VALUES ('0', '0104show');
INSERT INTO `sys_role_menu` VALUES ('0', '02');
INSERT INTO `sys_role_menu` VALUES ('0', '0201');
INSERT INTO `sys_role_menu` VALUES ('0', '0201del');
INSERT INTO `sys_role_menu` VALUES ('0', '0201edit');
INSERT INTO `sys_role_menu` VALUES ('0', '0201show');
INSERT INTO `sys_role_menu` VALUES ('0', '0202');
INSERT INTO `sys_role_menu` VALUES ('0', '0202del');
INSERT INTO `sys_role_menu` VALUES ('0', '0202edit');
INSERT INTO `sys_role_menu` VALUES ('0', '0202show');
INSERT INTO `sys_role_menu` VALUES ('12', '01');
INSERT INTO `sys_role_menu` VALUES ('12', '0102');
INSERT INTO `sys_role_menu` VALUES ('12', '0102del');
INSERT INTO `sys_role_menu` VALUES ('12', '0102edit');
INSERT INTO `sys_role_menu` VALUES ('12', '0102show');
INSERT INTO `sys_role_menu` VALUES ('12', '0103');
INSERT INTO `sys_role_menu` VALUES ('12', '0103del');
INSERT INTO `sys_role_menu` VALUES ('12', '0103edit');
INSERT INTO `sys_role_menu` VALUES ('12', '0103show');
INSERT INTO `sys_role_menu` VALUES ('12', '0104');
INSERT INTO `sys_role_menu` VALUES ('12', '0104del');
INSERT INTO `sys_role_menu` VALUES ('12', '0104edit');
INSERT INTO `sys_role_menu` VALUES ('12', '0104show');
INSERT INTO `sys_role_menu` VALUES ('12', '02');
INSERT INTO `sys_role_menu` VALUES ('12', '0201');
INSERT INTO `sys_role_menu` VALUES ('12', '0202');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `PK` varchar(40) NOT NULL,
  `LOGIN_NAME` varchar(40) DEFAULT NULL,
  `PHONE` varchar(40) DEFAULT NULL,
  `UP_NAME` varchar(40) DEFAULT NULL,
  `UP_PK` varchar(40) DEFAULT NULL,
  `PASSWORD` varchar(40) DEFAULT NULL,
  `NAME` varchar(40) DEFAULT NULL,
  `CARD_ID` varchar(40) DEFAULT NULL,
  `CARD_ADDRESS` varchar(100) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `RATIO` decimal(9,2) DEFAULT NULL,
  `BIRTHDAY` datetime DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `QQ` varchar(40) DEFAULT NULL,
  `ROLE_PK` int(11) DEFAULT NULL,
  `ACCOUNT_PK` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_PASSWORD` datetime DEFAULT NULL,
  `ENABLE` char(1) DEFAULT NULL,
  `FACE` varchar(100) DEFAULT NULL,
  `SEX` char(1) DEFAULT NULL COMMENT '0女1男',
  `REFERRER` varchar(40) DEFAULT NULL,
  `REFERRER_NAME` varchar(40) DEFAULT NULL,
  `REFERRER_PHONE` varchar(40) DEFAULT NULL,
  `ALIPAY` varchar(40) DEFAULT NULL,
  `WECHAT` varchar(40) DEFAULT NULL,
  `ROLE_NAME` varchar(40) DEFAULT NULL,
  `LOGIN_TIME` datetime DEFAULT NULL,
  `SALE_TOTAL` int(11) unsigned DEFAULT '0',
  `SCORE` int(11) unsigned NOT NULL DEFAULT '0',
  `RATIO_TOTAL` int(11) unsigned DEFAULT '0',
  PRIMARY KEY (`PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('0', 'root', '17704027977', null, null, '63a9f0ea7bb98050796b649e85481845', '雷勇', null, null, null, '305.00', null, null, null, '0', '0', null, null, null, '1', null, '1', null, null, null, null, null, 'admin', '2016-11-18 22:26:08', '9', '0', '405');