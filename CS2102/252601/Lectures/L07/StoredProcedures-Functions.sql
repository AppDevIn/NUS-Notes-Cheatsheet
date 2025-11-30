-- EXTRACT(component FROM date) extracts the component
-- AGE(dob) convert the date into another date encoding the age
-- EXTRACT(year FROM AGE(dob)) finds the age of a customer based on dob
SELECT c.customerid
FROM customers c
WHERE EXTRACT(year FROM AGE(dob)) < 21;


-- does not work
CREATE TABLE downloads(
  customerid VARCHAR(16)
    REFERENCES customers(customerid)
    ON UPDATE CASCADE ON DELETE CASCADE
    CHECK (customerid NOT IN (
      SELECT c.customerid FROM customers c NATURAL JOIN downloads d
      WHERE c.customerid = d.customerid AND d.name = 'Domainer'
      AND EXTRACT(year FROM AGE(dob)) < 21)),
  name VARCHAR(32),
  version CHAR(3),
  PRIMARY KEY (customerid, name, version),
  FOREIGN KEY (name, version)
    REFERENCES games(name, version)
    ON UPDATE CASCADE ON DELETE CASCADE
);


-- does not work
CREATE ASSERTION r21 CHECK (
  NOT EXISTS (
    SELECT c.customerid
    FROM customers c NATURAL JOIN downloads d
    WHERE d.name = 'Domainer'
      AND EXTRACT(year FROM AGE(dob)) < 21
  ))


-- calculation of age, still uses EXTRACT
-- but not using AGE function
CREATE OR REPLACE FUNCTION calculate_age(dob DATE)
RETURNS INTEGER AS $$
DECLARE
  years INTEGER;
BEGIN
  years := EXTRACT(year FROM CURRENT_DATE) - EXTRACT(year FROM dob);
  IF (EXTRACT(month FROM CURRENT_DATE) < EXTRACT(month FROM dob)) OR
     (EXTRACT(month FROM CURRENT_DATE) = EXTRACT(month FROM dob) AND
      EXTRACT(day FROM CURRENT_DATE) < EXTRACT(day FROM dob)) THEN
    years := years - 1;
  END IF;
  RETURN years;
END;
$$ LANGUAGE plpgsql;

SELECT calculate_age('2001-12-13');

SELECT c.customerid, calculate_age(c.dob) AS age
FROM customers c
ORDER BY age;


-- calculation of "age" using number of days
-- if age in years is required, still need to process further
CREATE OR REPLACE FUNCTION calculate_day(dob DATE)
RETURNS INTEGER AS $$
DECLARE
  days INTEGER;
BEGIN
  days := 0;
  WHILE dob < CURRENT_DATE LOOP
    days := days + 1;   -- increment integer
    dob := dob + 1;     -- increment date
  END LOOP;
  RETURN days;
END;
$$ LANGUAGE plpgsql;


-- find one of the most expensive games
CREATE OR REPLACE FUNCTION most_expensive()
RETURNS games AS $$
  SELECT *
  FROM games g
  ORDER BY g.price DESC
  LIMIT 1;
$$ LANGUAGE sql;

SELECT *
FROM most_expensive();


-- find one of the most downloaded games
CREATE OR REPLACE FUNCTION most_download
  (OUT name VARCHAR(32), OUT version CHAR(3))
RETURNS RECORD AS $$
  SELECT g.name, g.version
  FROM games g, downloads d
  WHERE g.name = d.name AND g.version = d.version
  GROUP BY g.name, g.version
  ORDER BY COUNT(*) DESC
  LIMIT 1;
$$ LANGUAGE sql;

SELECT *
FROM most_download();


-- find all of the most expensive games
CREATE OR REPLACE FUNCTION all_most_expensive()
RETURNS SETOF games AS $$
  SELECT *
  FROM games g
  GROUP BY g.name, g.version
  HAVING g.price >= ALL(
    SELECT g1.price FROM games g1
  );
$$ LANGUAGE sql;

SELECT *
FROM all_most_expensive();


-- find all of the most downloaded games
CREATE OR REPLACE FUNCTION all_most_download
  (OUT name VARCHAR(32), OUT version CHAR(3))
RETURNS SETOF RECORD AS $$
  SELECT g.name, g.version
  FROM games g, downloads d
  WHERE g.name = d.name AND g.version = d.version
  GROUP BY g.name, g.version
  HAVING COUNT(*) >= ALL (
    SELECT COUNT(*)
    FROM games g, downloads d
    WHERE g.name = d.name AND g.version = d.version
    GROUP BY g.name, g.version
  );
$$ LANGUAGE sql;

SELECT *
FROM all_most_download();


-- find all of the most downloaded games
CREATE OR REPLACE FUNCTION all_most_download()
  RETURNS TABLE(name VARCHAR(32), version CHAR(3))
AS $$
  SELECT g.name, g.version
  FROM games g, downloads d
  WHERE g.name = d.name AND g.version = d.version
  GROUP BY g.name, g.version
  HAVING COUNT(*) >= ALL ( /* ... */ )
$$ LANGUAGE sql;

SELECT *
FROM all_most_download();


-- find all customers who have downloaded a game
-- such that the customer is below the given age
CREATE OR REPLACE FUNCTION find_age
  (game VARCHAR(32), age INTEGER)
RETURNS SETOF customers AS $$
  SELECT * FROM customers c
  WHERE calculate_age(c.dob) < age
    AND c.customerid IN (
      SELECT d.customerid FROM downloads d
      WHERE d.name = game
  )
$$ LANGUAGE sql;

SELECT *
FROM find_age('Domainer', 21);


-- safe download (basic)
CREATE OR REPLACE PROCEDURE
  download_game(cid VARCHAR(16), gname VARCHAR(32), gver CHAR(3))
AS $$
BEGIN
    INSERT INTO downloads VALUES (cid, gname, gver);
END; $$ LANGUAGE plpgsql;


-- safe download (with local variable)
CREATE OR REPLACE PROCEDURE
  download_game(cid VARCHAR(16), gname VARCHAR(32), gver CHAR(3))
AS $$
DECLARE age INTEGER;
BEGIN
  SELECT calculate_age(c.dob) INTO age FROM customers c
  WHERE c.customerid = cid;
  IF gname = 'Domainer' AND age >= 21 THEN
    INSERT INTO downloads VALUES (cid, gname, gver);
  END IF;
END; $$ LANGUAGE plpgsql;


-- safe download (with IF NOT EXISTS(..))
CREATE OR REPLACE PROCEDURE
  download_game(cid VARCHAR(16), gname VARCHAR(32), gver CHAR(3))
AS $$
BEGIN
  IF NOT EXISTS (
    SELECT c.customerid FROM customers c
    WHERE gname = 'Domainer' AND calculate_age(c.dob) < 21
      AND c.customerid = cid ) THEN
    INSERT INTO downloads VALUES (cid, gname, gver);
  END IF;
END; $$ LANGUAGE plpgsql;


-- safe download (insert first then delete)
CREATE OR REPLACE PROCEDURE
  download_game(cid VARCHAR(16), gname VARCHAR(32), gver CHAR(3))
AS $$
BEGIN
    INSERT INTO downloads VALUES (cid, gname, gver);
    DELETE FROM downloads d WHERE d.customerid IN (
      SELECT c.customerid FROM customers c NATURAL JOIN downloads d1
      WHERE name = 'Domainer' AND calculate_age(c.dob) < 21
    );
END; $$ LANGUAGE plpgsql;


-- cursor
CREATE OR REPLACE FUNCTION max_increase(gname VARCHAR(32))
RETURNS NUMERIC AS $$
DECLARE
  cur CURSOR (vname VARCHAR(32)) FOR
      SELECT g.price FROM games g WHERE g.name = vname
      ORDER BY g.version ASC;
  res NUMERIC;  prev NUMERIC;  curr NUMERIC;
BEGIN
  OPEN cur(vname := gname);
  res := 0;  prev := 0;
  LOOP
    FETCH cur INTO curr;
    EXIT WHEN NOT FOUND;
    IF (curr - prev) >= res THEN res := (curr - prev); END IF;
    prev := curr;
  END LOOP;
  CLOSE cur;
  RETURN res;
END; $$ LANGUAGE plpgsql;
