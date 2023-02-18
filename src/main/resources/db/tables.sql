-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(255)                       null comment '用户昵称',
    userAccount  varchar(255)                       null comment '账号',
    avatar       varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(255)                       null comment '密码',
    phone        varchar(128)                       null comment '手机号',
    email        varchar(255)                       null comment '邮箱',
    userStatus   tinyint  default 0                 not null comment '用户状态',
    userRole     int      default 0                 not null comment '用户角色 （0-普通用户）（1-管理员）（2-最高管理员）',
    isDelete     tinyint  default 0                 not null comment '是否删除(逻辑删除)',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间'
)
    comment '用户表';
