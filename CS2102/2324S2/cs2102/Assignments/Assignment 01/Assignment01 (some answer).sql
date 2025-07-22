/*
Question 1

Find the different team name that retired in 2023.  In other words,
the team participated in some contest in 2023 but they did not participate
in any constest in 2024.  Order the result in ascending order of team name.

Rationale:
  Test simple algebraic query using EXCEPT.  Unfortunately, this cannot be
  ordered easily.  To do that, students need to use nested query in the
  FROM clause.
  Students may also use nested query using NOT IN but with careful use of
  DISTINCT keyword.
*/

-- Solution #1: Using EXCEPT
SELECT t.team
FROM (
  SELECT p.team
  FROM participate p
  WHERE p.year = 2023
  EXCEPT
  SELECT p.team
  FROM participate p
  WHERE p.year = 2024
) t
ORDER BY t.team;

-- Solution #2: Using nested query
/* Team Participating in 2023 */
SELECT DISTINCT p1.team
FROM participate p1
WHERE p1.year = 2023
  AND p1.team NOT IN (  -- but not here
    /* Team Participating in 2024 */
    SELECT p2.team
    FROM participate p2
    WHERE p2.year = 2024
  )
ORDER BY p1.team;



/*
Question 2

Reconstruct the scoreboard for the contest at 'Central Europe' in 2023.  In
other words, sort the data according to the ICPC ranking.  Show only the
team name and the number of problems they solve.

Rationale:
  Test sorting on multiple columns, including columns that do not appear in
  the resulting relation.
*/

SELECT p.team, p.solve
FROM participate p
WHERE p.site = 'Central Europe'
  AND p.year = 2023
ORDER BY p.solve DESC,
         p.time ASC,
         p.last ASC,
         p.team ASC;



/*
Question 3

Find all the teams that solves the most number of questions in the contest
that they participated in.  Show only the team name and the university name.
Order the result in descending order of University name followed by
ascending order of team name.

Some team may actually solve the most number of questions in multiple
contests.  In this case, the team will appear multiple times.  We exclude
team that do not participate in any contest.

Rationale:
  Test on finding the largest in with all ties shown.  Note that it cannot
  be easily solved using GROUP BY as it will only produce a single group.
  It cannot be done easily with LIMIT as we may not know the number of
  teams in the result.
*/

SELECT t1.name AS team, t1.univ AS university
FROM team t1, participate p1
WHERE t1.name = p1.team
  AND p1.solve >= ALL (
    /* The number of solves for a given site (p1.site) and the given
        year (p1.year).  The outer query must be greater than or 
        equal to all of these values. */
    SELECT p2.solve
    FROM participate p2
    WHERE p2.site = p1.site
      AND p2.year = p1.year
  )
ORDER BY t1.univ DESC, t1.name ASC;



/*
Question 4

Find the different team name that managed to solve all problems in at least
one contest they participated in.  Order the result in ascending order of
team name.

Rationale:
  Test on 'universal quantification' but on a different setting.  A typical
  universal quantification has doubly nested "NOT EXISTS", which is not
  applicable here.  Here, we simply have to check the COUNT.

  The reason it is not applicable is we do not know the exact problems each
  team has solved.  This is intended to provide an alternative solution to
  the universal quantification.  However, in a typical setting, the COUNT
  may need to be done separately using CTE.
*/

SELECT DISTINCT t1.name
FROM team t1
WHERE EXISTS (
  /* Team that solves (p1.solves) exactly the given amount as in inner query */
  SELECT *
  FROM participate p1
  WHERE p1.team = t1.name
    AND p1.solve = ALL (  -- ALL is not necessary as there is only 1 value
                          -- added as good practice
      /* The number of questions in a given site (p1.site) and year (p1.year) */
      SELECT COUNT(*)
      FROM question q1
      WHERE q1.site = p1.site
        AND q1.year = p1.year
    )
)
ORDER BY t1.name;


/*
For each region, find all the Universities that has sent the most number of
distinct team name to participate in contest.  Note the following important
points:
(i)  we only consider teams that participate in at least one contest, and
(ii) we only count each team name exactly once.

In particular, if team A is sent to two different sites, we only count it
once.  Similarly, if team B is sent in both 2023 and 2024, we still only
count it once.

Exclude region without university and region without contest.  Some regions
may have multiple univerities with the same number of teams sent.  In this
case, the region will appear multiple times.  Show the region name,
university name, and the number of distinct team name the university has
sent.

Rationale:
  Test on nested aggregation where we require two different grouping.  Here, we provide some potential query which unfortunately is incorrect.
*/

SELECT r.name AS region, u.name AS university, COUNT(DISTINCT p.team) AS count
FROM region r, university u, team t, participate p
WHERE r.name = u.region
  AND t.univ = u.name
  AND p.team = t.name
GROUP BY r.name, u.name
HAVING COUNT(DISTINCT p.team) >= ALL (
  SELECT COUNT(DISTINCT p1.team) AS count
  FROM region r1, university u1, team t1, participate p1
  WHERE r1.name = u1.region
    AND t1.univ = u1.name
    AND r1.name = r.name
    AND p1.team = t1.name
  GROUP BY r1.name, u1.name
);
-- ORDER BY count DESC;   -- if you want a nice output

-- Incorrect #1: This counts non-DISTINCT team name.
SELECT r.name AS region, u.name AS university, COUNT(p.team) AS count
FROM region r, university u, team t, participate p
WHERE r.name = u.region
  AND t.univ = u.name
  AND p.team = t.name
GROUP BY r.name, u.name
HAVING COUNT(p.team) >= ALL (
  SELECT COUNT(p1.team) AS count
  FROM region r1, university u1, team t1, participate p1
  WHERE r1.name = u1.region
    AND t1.univ = u1.name
    AND r1.name = r.name
    AND p1.team = t1.name
  GROUP BY r1.name, u1.name
);
-- ORDER BY count DESC;   -- if you want a nice output

-- Incorrect #2: This also counts non-DISTINCT team name AND it counts
--               teams that are not sent to any contest (currently none).
SELECT r.name, u.name, COUNT(p.team) AS count
FROM region r, university u, team t, participate p
WHERE r.name = u.region
  AND t.univ = u.name
  AND p.team = t.name
GROUP BY r.name, u.name
HAVING COUNT(p.team) >= ALL (
  SELECT COUNT(p1.team) AS count
  FROM region r1, university u1, team t1, participate p1
  WHERE r1.name = u1.region
    AND t1.univ = u1.name
    AND r1.name = r.name
    AND p1.team = t1.name
  GROUP BY r1.name, u1.name
);
-- ORDER BY count DESC;   -- if you want a nice output

-- Incorrect #3: This counts the teams that are not sent to any contest.
--               It may look correct on this dataset as there are no team
--               not sent to any contest currently.  But if such a team is
--               added, then it will give incorrect result.
SELECT r.name, u.name, COUNT(t.name) AS count
FROM region r, university u, team t
WHERE r.name = u.region
  AND t.univ = u.name
GROUP BY r.name, u.name
HAVING COUNT(t.name) >= ALL (
  SELECT COUNT(t1.name) AS count
  FROM region r1, university u1, team t1
  WHERE r1.name = u1.region
    AND t1.univ = u1.name
    AND r1.name = r.name
  GROUP BY r1.name, u1.name
);
-- ORDER BY count DESC;   -- if you want a nice output