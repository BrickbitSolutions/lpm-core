ALTER TABLE LPM_USER ADD COLUMN SEAT_NUMBER INTEGER;
ALTER TABLE LPM_USER ADD CONSTRAINT U_SEAT_NUMBER UNIQUE (SEAT_NUMBER);