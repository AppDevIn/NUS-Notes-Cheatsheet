-- Check if a customer downloaded 'Domainer' and is under 21
CREATE OR REPLACE FUNCTION is_r21()
RETURNS BOOLEAN AS $$
BEGIN
  IF NOT EXISTS (
    SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE c.customerid = d.customerid AND d.name = 'Domainer'
    AND EXTRACT(year FROM AGE(c.dob)) < 21
  ) THEN
    RETURN True;
  ELSE
    RETURN False;
  END IF;
END; $$ LANGUAGE plpgsql;


-- Try to alter the table
ALTER TABLE downloads
ADD CONSTRAINT is_r21 CHECK (is_r21());


-- Delete offending rows first
DELETE FROM downloads d0
WHERE d0.name = 'Domainer'
  AND d0.customerid IN (
	SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE c.customerid = d.customerid AND d.name = 'Domainer'
    AND EXTRACT(year FROM AGE(c.dob)) < 21
);


-- Retry to alter the table
ALTER TABLE downloads
ADD CONSTRAINT is_r21 CHECK (is_r21());


-- Check underage customers
SELECT c.customerid
FROM customers c
WHERE EXTRACT(year FROM AGE(c.dob)) < 21;


-- 'Jonathan2000' is underaged
INSERT INTO downloads VALUES ('Jonathan2000', 'Domainer', '1.0');
INSERT INTO downloads VALUES ('Jonathan2000', 'Aerified', '1.0');


-- Delete offending rows again before continuing
DELETE FROM downloads d0
WHERE d0.name = 'Domainer'
  AND d0.customerid IN (
	SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE c.customerid = d.customerid AND d.name = 'Domainer'
    AND EXTRACT(year FROM AGE(c.dob)) < 21
);


-- Drop the constraint
ALTER TABLE downloads
DROP CONSTRAINT is_r21;


-- Create Trigger Function + Trigger
CREATE OR REPLACE FUNCTION fr21()
RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (
    SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE d.name = 'Domainer'
      AND EXTRACT(year FROM AGE(c.dob)) < 21
  ) THEN
    RAISE EXCEPTION 'Underaged!'; -- STOP!
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER tr21
AFTER INSERT OR UPDATE ON downloads
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW
EXECUTE PROCEDURE fr21();


-- Test Triggers (pass)
INSERT INTO downloads VALUES ('Deborah84', 'Domainer', '1.0');
INSERT INTO downloads VALUES ('Deborah84', 'Aerified', '1.0');
INSERT INTO downloads VALUES ('Jonathan2000', 'Aerified', '1.0');

-- Test Triggers (fail)
INSERT INTO downloads VALUES ('Jonathan2000', 'Domainer', '1.0');
UPDATE downloads
SET name = 'Domainer'
WHERE customerid = 'Jonathan2000'
  AND name = 'Aerified';

-- Other tests (currently pass when it should not)
UPDATE customers
SET dob = '1990-01-01'
WHERE customerid = 'Jonathan2000';

UPDATE customers
SET dob = '2024-01-01'
WHERE customerid = 'Deborah84';


-- Logging Trigger
CREATE TABLE downloads_log (
  customerid VARCHAR(16),
  name VARCHAR(32),
  operation TEXT,
  date DATE
);

CREATE OR REPLACE FUNCTION log_ops()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO downloads_log VALUES
    (NEW.customerid, NEW.name,
     TG_OP, CURRENT_DATE);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER op_log
AFTER INSERT OR DELETE OR UPDATE
  ON downloads
FOR EACH ROW
  EXECUTE FUNCTION log_ops();

-- Test
INSERT INTO downloads VALUES ('Jonathan2000', 'Fixflex', '1.0');


-- Delete offending rows again before continuing
DELETE FROM downloads d0
WHERE d0.name = 'Domainer'
  AND d0.customerid IN (
	SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE c.customerid = d.customerid AND d.name = 'Domainer'
    AND EXTRACT(year FROM AGE(c.dob)) < 21
);

-- Retest
INSERT INTO downloads VALUES ('Jonathan2000', 'Fixflex', '1.0');
SELECT * FROM downloads_log;


-- Drop trigger + trigger functions first before continuing
DROP TRIGGER tr21 ON downloads;
DROP FUNCTION fr21();


-- Create another trigger function + before trigger
CREATE OR REPLACE FUNCTION fr21()
RETURNS TRIGGER AS $$
BEGIN
  IF (NOT is_r21()) THEN
    RAISE EXCEPTION 'Still underaged!'; -- STOP!
  END IF;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER tr21
BEFORE INSERT ON downloads
FOR EACH ROW EXECUTE FUNCTION fr21();

-- Test (with new data)
INSERT INTO downloads VALUES ('Deborah84', 'Domainer', '1.0');
INSERT INTO downloads VALUES ('Jonathan2000', 'Fixflex', '1.0');


-- Delete offending rows again before continuing
DELETE FROM downloads d0
WHERE d0.name = 'Domainer'
  AND d0.customerid IN (
	SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE c.customerid = d.customerid AND d.name = 'Domainer'
    AND EXTRACT(year FROM AGE(c.dob)) < 21
);


-- Drop & create another trigger function + before trigger
DROP TRIGGER tr21 ON downloads;
DROP FUNCTION fr21();

CREATE OR REPLACE FUNCTION get_age(cid VARCHAR(16))
RETURNS INTEGER AS $$
DECLARE
  years INTEGER;
BEGIN
  SELECT EXTRACT(year FROM AGE(c.dob)) INTO years
  FROM customers c
  WHERE c.customerid = cid;
  RETURN years;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION fr21()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.name = 'Domainer' AND
     get_age(NEW.customerid) < 21 THEN
    RAISE EXCEPTION 'Underaged!'; -- STOP!
  END IF;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER tr21
BEFORE INSERT ON downloads
FOR EACH ROW EXECUTE FUNCTION fr21();


-- Drop & create another trigger function + before trigger
DROP TRIGGER tr21 ON downloads;
DROP FUNCTION fr21();

CREATE OR REPLACE FUNCTION fr21()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.name = 'Domainer' AND
     get_age(NEW.customerid) < 21 THEN
    RETURN NULL; -- STOP!
  END IF;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER tr21
BEFORE INSERT ON downloads
FOR EACH ROW EXECUTE FUNCTION fr21();


-- Statement level trigger
CREATE OR REPLACE FUNCTION no_delete()
RETURNS TRIGGER AS $$
BEGIN
  RAISE EXCEPTION 'No delete from log...';
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER warn_delete
BEFORE DELETE ON downloads_log
FOR EACH STATEMENT
EXECUTE FUNCTION no_delete();

-- Test
DELETE FROM downloads_log;