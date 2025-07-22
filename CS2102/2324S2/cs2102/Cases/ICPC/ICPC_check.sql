SELECT
	(SELECT COUNT(*) FROM region) = 3 AS region,
	(SELECT COUNT(*) FROM site) = 16 AS site,
	(SELECT COUNT(*) FROM contest) = 27 AS contest,
	(SELECT COUNT(*) FROM question) = 346 AS question,
	(SELECT COUNT(*) FROM university) = 606 AS university,
	(SELECT COUNT(*) FROM team) = 2325 AS team,
	(SELECT COUNT(*) FROM participate) = 2556 AS participate;
