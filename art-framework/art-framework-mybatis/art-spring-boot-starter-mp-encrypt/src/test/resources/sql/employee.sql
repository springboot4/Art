CREATE TABLE IF NOT EXISTS employee
(
    `id`       int PRIMARY KEY NOT NULL,
    `name`     varchar(50)     NOT NULL DEFAULT 'oop',
    `age`      smallint(4) DEFAULT NULL,
    `gender`   char(1)                  DEFAULT NULL,
    `motto`    varchar(255)             DEFAULT NULL,
    `birthday` datetime                 DEFAULT NULL,
    `hobby`    varchar(255)             DEFAULT NULL
);