-- Oracle DDL for the 'girls' table based on GirlRepositoryImpl

-- Create sequence for auto-incrementing girl_id
CREATE SEQUENCE girls_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Create the girls table
-- Note: Character set is defined at the database level, typically using AL32UTF8 for UTF-8 support
CREATE TABLE girls (
    girl_id NUMBER(10) PRIMARY KEY,
    girl_name VARCHAR2(255) NOT NULL
)
TABLESPACE USERS
STORAGE (INITIAL 1M NEXT 1M)
NOLOGGING;

-- Create trigger to auto-increment girl_id
CREATE OR REPLACE TRIGGER girls_bi_trg
    BEFORE INSERT ON girls
    FOR EACH ROW
BEGIN
    IF :new.girl_id IS NULL THEN
        SELECT girls_seq.NEXTVAL INTO :new.girl_id FROM dual;
    END IF;
END;
/

-- Add comments for documentation
COMMENT ON TABLE girls IS 'Table storing information about girls';
COMMENT ON COLUMN girls.girl_id IS 'Primary key identifier for girls';
COMMENT ON COLUMN girls.girl_name IS 'Name of the girl';