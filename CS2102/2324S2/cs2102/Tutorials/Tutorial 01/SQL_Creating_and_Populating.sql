INSERT INTO book VALUES (
'An Introduction to Database Systems',
'paperback' , 
640 , 
'English' , 
'C. J. Date' , 
'Pearson',
'2003-01-01' , 
'0321197844' , 
'978-0321197849');

SELECT *
FROM book;

INSERT INTO book VALUES (
'An Introduction to Database Systems', 
'paperback', 
640,
'English',
'C.J. Date', 
'Pearson', 
'2003-01-01', 
'0321197844',  
'978-0201385908');

INSERT INTO book VALUES (
'An Introduction to Database Systems', 
'hardcover',
938,
'English',
'C.J. Date',
'Addison Wesley Longman',
'2000-01-01',
'0201385902',
'978-0321197849');

INSERT INTO student VALUES (
'TIKKI TAVI' , 
'tikki@gmail.com' , 
'2024-08-15', 
'School of Computing', 
'CS', 
NULL);

INSERT INTO student (email, name, year, faculty, department)
VALUES (
'rikki@gmail.com', 
'RIKKI TAVI', 
'2024-08-15', 
'School of Computing', 
'CS');

INSERT INTO student (name, year, faculty, department) 
VALUES (
'RIKKI TAVI',  
'2024-08-15', 
'School of Computing', 
'CS');

UPDATE student
SET department = 'Computer Science'
WHERE department = 'CS';

SELECT *
FROM student
WHERE department = 'CS';

SELECT *
FROM student
WHERE department = 'Computer Science';

DELETE FROM student 
WHERE department = 'chemistry';

DELETE FROM student 
WHERE department = 'Chemistry';

INSERT INTO copy
VALUES (
'tikki@gmail.com', 
'978-0321197849', 
1, 
'TRUE') ;

BEGIN TRANSACTION;
SET CONSTRAINTS ALL IMMEDIATE;
DELETE FROM book 
WHERE ISBN13 = '978-0321197849' ;
DELETE FROM copy 
WHERE book = '978-0321197849' ;
END TRANSACTION;

END TRANSACTION;

BEGIN TRANSACTION;
SET CONSTRAINTS ALL DEFERRED;
DELETE FROM book WHERE ISBN13 = '978-0321197849' ;
DELETE FROM copy WHERE book = '978-0321197849' ;
END TRANSACTION;

SELECT owner, book, copy, returned 
FROM loan 
WHERE returned IS NULL;

ALTER TABLE copy
DROP COLUMN available;

CREATE OR REPLACE VIEW copy_view (owner, book, copy, available) 
AS 
(SELECT DISTINCT c.owner, c.book, c.copy, 
CASE
WHEN EXISTS (SELECT * FROM loan l  
WHERE l.owner = c.owner
AND l.book = c.book
AND l.copy = c.copy AND l.returned ISNULL) THEN 'FALSE'
ELSE 'TRUE' 
END
FROM copy c);

SELECT * FROM copy_view;

UPDATE copy_view 
SET owner = 'tikki@google.com' 
WHERE owner = 'tikki@gmail.com'

DROP VIEW copy_view;

CREATE TABLE department (
department VARCHAR(32) PRIMARY KEY,
faculty VARCHAR(62) NOT NULL);

INSERT INTO department 
SELECT DISTINCT department, faculty 
FROM student;

ALTER TABLE student
DROP COLUMN faculty;

ALTER TABLE student
ADD FOREIGN KEY (department) REFERENCES department(department);
