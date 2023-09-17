create database if not exists socialdb default character set utf8mb4;

drop table `sys_user`;
CREATE TABLE `sys_user`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `phone` varchar(15)  DEFAULT NULL COMMENT '电话',
    `password` varchar(200) DEFAULT NULL COMMENT '密码',
    `avatar_url` varchar(500) COMMENT '头像',
    `nick_name` varchar(50) DEFAULT '默认用户' COMMENT '昵称',
    `openid` varchar(200),
    `session_key` varchar(200),
    `unionid` varchar(200) DEFAULT NULL,
    `sex` tinyint(1) DEFAULT '1' COMMENT '性别，0女1男',
    `intro` varchar(256) COMMENT '简介',
    `create_time` datetime,
    `update_time` datetime,
    PRIMARY KEY (`id`),
    UNIQUE KEY `un_1` (`phone`),
    UNIQUE KEY `un_2` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;