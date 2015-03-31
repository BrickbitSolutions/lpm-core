-- // create_authorities_table
-- Migration SQL that makes the change goes here.

CREATE TABLE AUTHORITY (
    ID INTEGER NOT NULL AUTO_INCREMENT,
    AUTHORITY VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID),
    UNIQUE (AUTHORITY)
);

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE AUTHORITY;