-- Q1 Simple Test
CALL borrow_book_proc('awong007@msn.com', '978-1449389673',
  CURRENT_DATE);


-- Q1a Trigger Function
CREATE OR REPLACE FUNCTION check_local_loan_limit()
RETURNS TRIGGER AS $$
DECLARE
  active_loan_count INT;
BEGIN
  -- Count the number of active loans (not yet returned)
  SELECT COUNT(*) INTO active_loan_count
  FROM loan l
  WHERE l.borrower = NEW.borrower
    AND l.returned ISNULL;
  
  IF active_loan_count >= 6
  THEN
    RETURN NULL; -- prevent borrowing
  ELSE
    RETURN NEW;  -- allow borrowing
  END IF;
END;
$$ LANGUAGE plpgsql;


-- Q1a Trigger
CREATE TRIGGER enforce_local_loan_limit_insert
BEFORE INSERT ON loan
FOR EACH ROW EXECUTE FUNCTION check_local_loan_limit();


-- Q1a DROP TRIGGER
DROP TRIGGER enforce_local_loan_limit_insert ON loan;
DROP FUNCTION check_local_loan_limit();


-- Q1b Trigger Function
CREATE OR REPLACE FUNCTION check_global_loan_limit()
RETURNS TRIGGER AS $$
DECLARE
  violating_student RECORD;
BEGIN
  -- Check if there is any student with more than 6 active loans
  SELECT l.borrower INTO violating_student
  FROM loan l
  WHERE l.returned ISNULL
  GROUP BY l.borrower
  HAVING COUNT(*) > 6;

  IF violating_student IS NOT NULL
  THEN -- There is a violation, raise exception
    RAISE EXCEPTION '% has borrowed more than 6 books',
      violating_student;
  ELSE
    RETURN NEW;
  END IF;
END;
$$ LANGUAGE plpgsql;



-- Q1b Trigger
CREATE TRIGGER enforce_global_loan_limit
AFTER INSERT OR UPDATE ON loan
FOR EACH ROW EXECUTE FUNCTION check_global_loan_limit();


-- Q1b DROP TRIGGER
DROP TRIGGER enforce_global_loan_limit ON loan;
DROP FUNCTION check_global_loan_limit();


-- Q1 DROP SCHEMA
-- change `public` to the name of your schema
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;


-- Q2a Trigger Function
CREATE OR REPLACE FUNCTION graduation_date()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.graduate > CURRENT_DATE
  THEN
    NEW.graduate := CURRENT_DATE;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Q2a Trigger
CREATE TRIGGER no_future_graduation
BEFORE INSERT OR UPDATE ON student
FOR EACH ROW EXECUTE FUNCTION graduation_date();


