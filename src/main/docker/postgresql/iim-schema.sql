CREATE DATABASE iim;
CREATE USER iimuser WITH PASSWORD 'Iimuser@123';
GRANT ALL PRIVILEGES ON DATABASE iim TO iimuser;

\c iim iimuser

CREATE SCHEMA iim;

CREATE TABLE iim.iim.reboot (host varchar(255) DEFAULT NULL, restartCount int DEFAULT NULL, LastRebootTime timestamp DEFAULT NULL, AlertOps varchar(3) DEFAULT NULL, Restart varchar(3) DEFAULT NULL);

CREATE TABLE iim.iim.reboot_audit (host_id varchar(255) DEFAULT NULL, requestCount int DEFAULT NULL, maxRestartCount int DEFAULT NULL, requestTime timestamp NOT NULL, alertOps varchar(3) DEFAULT NULL, restart varchar(3) DEFAULT NULL);

CREATE TABLE iim.iim.rebootconfig (id SERIAL PRIMARY KEY, rMinutes int NOT NULL, rCount int DEFAULT NULL, ruleId varchar(45) NOT NULL);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA iim TO iimuser;

ALTER ROLE iimuser SET search_path TO iim, public;

INSERT INTO iim.rebootconfig VALUES(1,5,10,'reboot');