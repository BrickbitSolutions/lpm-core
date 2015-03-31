-- // DML_Add_Authorities
-- Migration SQL that makes the change goes here.

INSERT INTO AUTHORITY(AUTHORITY) VALUES ('ROLE_USER');
INSERT INTO AUTHORITY(AUTHORITY) VALUES ('ROLE_ADMIN');

-- //@UNDO
-- SQL to undo the change goes here.

DELETE FROM AUTHORITY;
