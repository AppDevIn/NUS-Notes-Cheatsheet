SELECT d.department
FROM department d;

SELECT DISTINCT s.department
FROM student s;

SELECT DISTINCT s.email 
FROM loan l, student s 
WHERE (s.email = l.borrower OR s.email = l.owner) 
AND l.borrowed < s.year;

SELECT DISTINCT s.email 
FROM loan l, student s 
WHERE (s.email = l.borrower AND l.borrowed < s.year) 
OR (s.email = l.owner AND l.borrowed < s.year);


SELECT book, returned - borrowed + 1 AS duration 
FROM loan
WHERE returned IS NOT NULL
ORDER BY book ASC, duration DESC;

SELECT book, returned - borrowed + 1 AS duration 
FROM loan
WHERE NOT (returned ISNULL)
ORDER BY book ASC, duration DESC;

SELECT book, (COALESCE(returned, CURRENT_DATE) - borrowed + 1) AS duration 
FROM loan
ORDER BY book ASC, duration ASC;

SELECT book, 
	((CASE
	WHEN returned ISNULL 
	THEN CURRENT_DATE
	ELSE returned
	END) - borrowed + 1) AS duration 
FROM loan
ORDER BY book ASC, duration ASC;

SELECT b.title, 
	s1.name AS ownerName, 
	d1.faculty AS ownerFaculty, 
	s2.name AS borrowerName, 
	d2.faculty AS borrowerFaculty
FROM loan l, book b,  copy c, 
	student s1, student s2, 
	department d1, department d2
WHERE l.book=b.ISBN13
	AND c.book = l.book 
	AND c.copy = l.copy 
	AND c.owner = l.owner
	AND l.owner = s1.email
	AND l.borrower = s2.email
	AND s1.department = d1.department
	AND s2.department = d2.department
	AND b.publisher ='Wiley'
	AND l.returned ISNULL;

SELECT b.title, 
	s1.name AS ownerName, 
	d1.faculty AS ownerFaculty, 
	s2.name AS borrowerName, 
	d2.faculty AS  borrowerFaculty
FROM loan l 
	INNER JOIN book b ON l.book=b.ISBN13
	INNER JOIN copy c ON c.book = l.book 
		AND c.copy = l.copy 
		AND c.owner = l.owner
	INNER JOIN student s1 ON l.owner = s1.email
	INNER JOIN student s2 ON l.borrower = s2.email
	INNER JOIN department d1 ON s1.department = d1.department
	INNER JOIN department d2 ON s2.department = d2.department
WHERE  b.publisher ='Wiley'
	AND l.returned ISNULL;

SELECT b.title, 
	s1.name AS ownerName, 
	d1.faculty AS ownerFaculty, 
	s2.name AS borrowerName, 
	d2.faculty AS  borrowerFaculty
FROM loan l 
	INNER JOIN book b ON l.book=b.ISBN13
	INNER JOIN student s1 ON l.owner = s1.email
	INNER JOIN student s2 ON l.borrower = s2.email
	INNER JOIN department d1 ON s1.department = d1.department
	INNER JOIN department d2 ON s2.department = d2.department
	WHERE  b.publisher ='Wiley'
	AND l.returned ISNULL;

SELECT  s.email 
FROM loan l, student s 
WHERE s.email = l.borrower AND l.borrowed = s.year
UNION
SELECT  s.email 
FROM loan l, student s 
WHERE s.email = l.owner AND l.borrowed = s.year;

SELECT DISTINCT s.email 
FROM loan l,  student s 
WHERE (s.email = l.borrower OR s.email = l.owner) 
AND l.borrowed = s.year;

SELECT  s.email 
FROM loan l, student s 
WHERE s.email = l.borrower AND l.borrowed = s.year
INTERSECT
SELECT  s.email 
FROM loan l, student s 
WHERE s.email = l.owner AND l.borrowed = s.year;

SELECT DISTINCT s.email 
FROM loan l1, loan l2, student s 
WHERE s.email = l1.borrower AND l1.borrowed = s.year 
AND s.email = l2.owner AND l2.borrowed = s.year;

SELECT  b.ISBN13 
FROM book b
EXCEPT
SELECT  l.book 
FROM loan l;

SELECT  b.ISBN13 
FROM book b LEFT OUTER JOIN loan l ON b.isbn13 = l.book
WHERE l.book ISNULL;

