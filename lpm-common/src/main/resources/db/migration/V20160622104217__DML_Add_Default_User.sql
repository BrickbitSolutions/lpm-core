INSERT INTO LPM_USER(username, password, firstname, lastname, email, nonexpired, nonlocked, credsnonexpired, enabled)
VALUES ('admin', '$2a$10$asqus3jJEvovoibX5vyhGe.kk65OMkKRv19bzlKLczxJM7Xs0dBY6', 'Default', 'Admin', 'mail@mail.be', true, true, true, true);

INSERT INTO USER_AUTHORITY (lpm_user_id, authority_id) VALUES (
  (SELECT ID FROM LPM_USER WHERE USERNAME LIKE 'admin'),
  (SELECT ID FROM AUTHORITY WHERE AUTHORITY LIKE 'ROLE_USER')
);

INSERT INTO USER_AUTHORITY (lpm_user_id, authority_id) VALUES (
  (SELECT ID FROM LPM_USER WHERE USERNAME LIKE 'admin'),
  (SELECT ID FROM AUTHORITY WHERE AUTHORITY LIKE 'ROLE_ADMIN')
);