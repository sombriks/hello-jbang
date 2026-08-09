--liquibase formatted sql
--changeset sombriks:1
CREATE TABLE todo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    concluido BOOLEAN DEFAULT FALSE
);
