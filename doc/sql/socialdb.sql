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
    UNIQUE KEY uk_phone (`phone`),
    UNIQUE KEY uk_openid (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ss_post`
(
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户id',
    `content` text NOT NULL COMMENT '内容',
    `img_url_list` json DEFAULT NULL COMMENT '图片列表',
    `like_count` int DEFAULT '0' COMMENT '点赞数',
    `del_flg` tinyint DEFAULT '0' COMMENT '删除标志',
    `create_time` datetime COMMENT '创建时间',
    `update_time` datetime COMMENT '修改时间',
    PRIMARY KEY (`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `ss_like` (
    `id` bigint(20) NOT NULL,
    `item_id` bigint(20) NOT NULL COMMENT '点赞条目id',
    `user_id` bigint(20) NOT NULL COMMENT '用户id',
    `like` tinyint(1) NOT NULL COMMENT '是否点赞，true点赞，false未点赞',
    `create_time` datetime COMMENT '创建时间',
    `update_time` datetime COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id_item_id` (`user_id`, `item_id`),
    KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;