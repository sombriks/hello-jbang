--liquibase formatted sql
--changeset sombriks:2
INSERT INTO todo (titulo, concluido)
VALUES ('Estudar JBang com Spring Boot 4', false);
INSERT INTO todo (titulo, concluido)
VALUES ('Configurar Liquibase modular', true);
