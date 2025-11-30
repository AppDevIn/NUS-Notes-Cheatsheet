-- Q1a Stored Function
CREATE OR REPLACE FUNCTION borrow_book_func (
  borrower_email VARCHAR (256), isbn13 CHAR (14), borrow_date DATE
) RETURNS TEXT AS $$
DECLARE
  available_copy RECORD ;
BEGIN
  -- Check for a copy of the book that is not currently borrowed
  --   (i.e., no active loan)
  SELECT * INTO available_copy
  FROM copy c
  WHERE c.book = isbn13
    AND NOT EXISTS (
      SELECT 1 FROM loan l
      WHERE l.book = c.book
        AND l.copy = c.copy
        AND l.owner = c.owner
        AND l.returned IS NULL
    )
  LIMIT 1;
  
  IF NOT FOUND -- No available copy found, return a message
  THEN
    RETURN 'No available copies of the book with ISBN13 : ' || isbn13;
  ELSE -- An available copy found
    -- Insert a new record into the loan table to record the borrowing
    INSERT INTO loan (borrower, owner, book, copy, borrowed)
    VALUES (borrower_email, available_copy.owner,
      available_copy.book, available_copy.copy, borrow_date);
    -- Return a success message
    RETURN 'Book with ISBN13 : ' || isbn13 ||
      ' has been successfully borrowed by ' || borrower_email;
  END IF;
END;
$$ LANGUAGE plpgsql;


-- Q1a Invocation
SELECT borrow_book_func ('awong007@msn.com', '978-0470170526',
  CURRENT_DATE);
SELECT borrow_book_func ('awong007@msn.com', '978-0470170526',
  CURRENT_DATE);
SELECT borrow_book_func ('awong007@msn.com', '978-0470170526',
  CURRENT_DATE);


-- Q1b Stored Procedure
CREATE OR REPLACE PROCEDURE borrow_book_proc (
  borrower_email VARCHAR (256), isbn13 CHAR (14), borrow_date DATE
) AS $$
DECLARE
  available_copy RECORD;
BEGIN
  -- Check for a copy of the book that is not currently borrowed
  --   (i.e., no active loan)
  SELECT * INTO available_copy
  FROM copy c
  WHERE c.book = isbn13
    AND NOT EXISTS (
      SELECT 1 FROM loan l
      WHERE l.book = c.book
        AND l.copy = c.copy
        AND l.owner = c.owner
        AND l.returned ISNULL
    )
  LIMIT 1;

  IF NOT FOUND -- No available copy found, raise notice
  THEN
    RAISE NOTICE 'No available copies of the book with ISBN13 : %',
      isbn13;
    RETURN;
  ELSE -- An available copy found
    -- Insert a new record into the loan table to record the borrowing
    INSERT INTO loan (borrower, owner, book, copy, borrowed)
    VALUES (borrower_email, available_copy.owner,
      available_copy.book, available_copy.copy, borrow_date);
    -- Raise a success message
    RAISE NOTICE 'Book with ISBN13 : % has been successfully 
      borrowed by %', isbn13, borrower_email;
  END IF;
END;
$$ LANGUAGE plpgsql;


-- Q1b Invocation
CALL borrow_book_proc ('awong007@msn.com', '978-0470089156',
  CURRENT_DATE);
CALL borrow_book_proc ('awong007@msn.com', '978-0470089156',
  CURRENT_DATE);
CALL borrow_book_proc ('awong007@msn.com', '978-0470089156',
  CURRENT_DATE);
CALL borrow_book_proc ('awong007@msn.com', '978-0470089156',
  CURRENT_DATE);



-- Q2a Stored Function with CURSOR
CREATE OR REPLACE FUNCTION borrow_diff ()
RETURNS TABLE (email VARCHAR (256), diff BIGINT) AS $$
DECLARE
  curs CURSOR FOR (
    SELECT s.email AS email, COUNT(l.book) AS count
    FROM student s LEFT JOIN loan l
      ON s.email = l.borrower
    GROUP BY s.email
    ORDER BY count DESC
  );
  prev BIGINT;
  rec RECORD;
BEGIN
  prev := -1;
  OPEN curs;                        -- (1) Open cursor

  LOOP
    FETCH NEXT FROM curs INTO rec;  -- (2) Fetch from cursor
    EXIT WHEN NOT FOUND;            -- (3a) Not found: EXIT

    email := rec.email;             -- (3b) Found: COMPUTE
    IF prev = -1
    THEN
      diff := NULL;
      prev := rec.count;
      RETURN NEXT;
    ELSE
      diff := prev - rec.count;
      prev := rec.count;
      RETURN NEXT;
    END IF;
  END LOOP;

  CLOSE curs;                       -- (4) Close cursor
  RETURN;
END;
$$ LANGUAGE plpgsql;


