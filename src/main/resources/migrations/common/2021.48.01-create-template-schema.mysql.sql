-- liquibase formatted sql

-- changeset ammar raza:1634801352 comment OPS-609

CREATE TABLE user (id VARCHAR(200) NOT NULL, name VARCHAR(200) DEFAULT '' NOT NULL, phone_number VARCHAR(50) DEFAULT '' NOT NULL,created_by VARCHAR(200) DEFAULT '' NOT NULL, updated_by VARCHAR(200) DEFAULT '' NOT NULL, created_at timestamp DEFAULT NOW() NOT NULL, updated_at timestamp DEFAULT NOW() NOT NULL);