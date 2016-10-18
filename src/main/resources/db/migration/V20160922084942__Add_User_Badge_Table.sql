CREATE TABLE USER_BADGE (
  ID          BIGSERIAL,
  LPM_USER_ID BIGINT       NOT NULL,
  TOKEN       VARCHAR(250) NOT NULL UNIQUE,
  ENABLED     BOOLEAN DEFAULT FALSE,
  CONSTRAINT PK_USER_CARD PRIMARY KEY (ID),
  CONSTRAINT FK_USER_CARD_USER FOREIGN KEY (LPM_USER_ID) REFERENCES LPM_USER (ID)
)