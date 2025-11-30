# CS2102

## Preliminary

CREATE TABLE containers (
  cid   INT  PRIMARY KEY,
  cost  NUMERIC  NOT NULL  CHECK (cost > 0)
);

CREATE TABLE ships (
  sid   INT  PRIMARY KEY,
  cid   INT  UNIQUE NOT NULL REFERENCES containers (cid)
    ON UPDATE CASCADE
  dock  INT  NOT NULL
);

CREATE TABLE yards (
  yid         INT  PRIMARY KEY,
  max_height  INT  NOT NULL  CHECK (max_height > 0)
);

CREATE TABLE in_yards (
  cid     INT  REFERENCES containers (cid)
    ON UPDATE CASCADE,
  yid     INT  REFERENCES yards (yid)
    ON UPDATE CASCADE,
  height  INT  NOT NULL  CHECK (height > 0),
  PRIMARY KEY (cid, yid),
  UNIQUE (yid, height)
);

a) A container must be either in a yard (i.e., recorded in in_yards) or in a ship but not both.  It cannot be in neither.
b) A container's height in in_yards must be smaller than or equal to the corresponding max_height of the yard (i.e., as identified by yid).
c) A yard (i.e., as identified by yid) cannot have container (i.e., as recorded in in_yards) stacked with a gap in height.  For instance, it cannot have a container at height 1 and 3 without another container at height 2.
 

### Q2

CREATE OR REPLACE FUNCTION check_container_height()
RETURNS TRIGGER AS $$
BEGIN
  IF NOT EXISTS (
    SELECT * FROM in_yards y
    WHERE y.yid = NEW.yid
      AND y.height = NEW.height - 1
  ) THEN
    RETURN NULL;
  END IF;
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

"""
Every container must have a container below it.  (Issue with height = 1 since we cannot insert 0).
"""

2. The trigger function enforces constraint (c) partially as required.
3. The trigger function is too strict as we cannot insert any container to an empty yard.


### Q3

CREATE TRIGGER container_yard_height
BEFORE INSERT ON in_yards
FOR EACH ROW
EXECUTE PROCEDURE check_container_height();

CREATE OR REPLACE FUNCTION check_container_height()
RETURNS TRIGGER AS ​$​$
DECLARE
  ctxh INT;
  maxh INT;
BEGIN
  SELECT COUNT(y.height) INTO ctxh
  FROM in_yards y
  WHERE y.yid = NEW.yid AND y.height < NEW.height;

  SELECT y.max_height INTO maxh
  FROM yards y
  WHERE y.yid = NEW.yid;

  IF (ctxh = NEW.height - 1 AND NEW.height <= maxh) THEN
    RETURN NEW;
  ELSE
    RETURN NULL;
  END IF;
END;  ​$​$ LANGUAGE plpgsql;

"""
At insertion at height H, there must be H-1 containers below it.  The maximum height cannot exceed.
"""

1. The trigger function enforces constraint (b) partially as required.
2. The trigger function enforces constraint (c) partially as required.


### Q4 and Q5

CREATE CONSTRAINT TRIGGER yard_or_ship
AFTER INSERT OR UPDATE OR DELETE ON ___?___
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE PRODECURE either_yard_or_ship();

CREATE OR REPLACE FUNCTION either_yard_or_ship()
RETURNS TRIGGER AS ​$​$
BEGIN
  IF EXISTS (
    SELECT * FROM (
      (SELECT c1.cid FROM containers c1)
       UNION ALL
      (SELECT c2.cid FROM ships c2)
       UNION ALL
      (SELECT c3.cid FROM in_yards c3)
    ) AS t
    GROUP BY t.cid
    HAVING (t.cid) <> 2;
  ) THEN
    RAISE EXCEPTION 'error!';
  END IF;
END;  ​$​$ LANGUAGE plpgsql;

Idea: We check that the total count of containers in "containers", "ships", and "in_yards" are exactly 2.


### Q6

(a) CREATE TRIGGER in_yards_only AFTER INSERT ON in_yards FOR EACH ROW EXECUTE PROCEDURE check_only_in_yards();
(b) CREATE TRIGGER not_on_ships BEFORE INSERT ON in_yards FOR EACH ROW EXECUTE PROCEDURE check_not_on_ships();
(c) CREATE TRIGGER below_or_equal_max_height BEFORE INSERT ON in_yards FOR EACH ROW EXECUTE PROCEDURE check_max_height();
(d) CREATE TRIGGER not_height_gap AFTER INSERT ON in_yards FOR EACH STATEMENT EXECUTE PROCEDURE check_no_gap_in_height();

(c) ; (b) ; (a) ; (d)


### Q7

Invalid Algebra
A. Qans = π[sid,cid](ships) ∪ π[cid](containers)
  - Not union-compatible
D. Qans = π[cid](σ[max_height<=10](in_yards ⋈ containers))
  - No attribute "max_height" in "in_yards ⋈ containers"
E. Qans = π[yid](σ[height=1](yards ⋈ π[cid](in_yards)))
  - Attribute "height" is removed after π[cid](in_yards) and not present for σ[height=1]


### Q8

Equivalent
A. Q1 = π[yid,cid](σ[height<=3](in_yards) ⟕ yards)
   Q2 = π[yid,cid](σ[height<=3](in_yards ⋈ yards))
- Left outer join reduces to natural join as there can never be an entry in "in_yards" that is not in "yards" due to foreign key
- Since "height" is in "in_yards", we can push the σ[height<=3] to be performed first

C. Q1 = π[sid](σ[yid=1](ships) ⋈[cid=cid2] ρ(containers, c(cid -> cid2)))
   Q2 = π[sid](σ[yid=1](ships) ⋈ containers)
- From definition of natural join
- Common attribute is cid, so after renaming, need explicit equality

D. Q1 = π[sid](σ[dock=5](ships) ⋈[cid=cid2] ρ(containers, c(cid -> cid2)))
   Q2 = π[sid](σ[dock=5 ∧ cid=cid2](ships) × ρ(containers, c(cid -> cid2)))
- From definition of inner join translated to cross product and selection
- Selection can be combined with "and" operator


### Q9

SELECT y.yid
FROM yards y
WHERE y.max_height >= 10
AND NOT EXISTS ( SELECT 1
                 FROM in_yards i
                 WHERE i.yid = y.yid
                 AND i.height = y.max_height );

C. Q1 = σ[height=max_height](yards ⋈ in_yards)
   Qans = π[yid](σ[max_height>=10 ∧ height≡NULL](yards ⟕ Q1))
D. Q1 = σ[height=max_height](yards ⋈ in_yards)
   Qans = π[yid](σ[max_height>=10](yards)) - π[yid](Q1)


### Q10

Q = π[sid](π[sid,cid](ships) ⋈ π[cid](σ[cost>10000](containers)))

A.
SELECT s.sid
FROM ships s
WHERE cid = ANY (SELECT cid
                 FROM containers c
                 WHERE c.cost > 10000);

C.
(SELECT s.sid 
 FROM ships s)
INTERSECT
(SELECT s.sid
 FROM ships s, containers c
 WHERE s.cid = c.cid
   AND c.cost > 10000)

D.
SELECT s.sid
FROM ships s
WHERE s.cid IN (SELECT cid
                FROM containers c
                WHERE c.cost > 10000)
GROUP BY s.sid;



## Relational Algebra

### Q11

- R(a,b,c) with |R| = m
- S non-empty

Size at least m

A. R × S  (size is at least m × n with n > 0)
C. R ⟕ S  (size is at least m if all dangling)
E. R ∪ S  (size is at least m if S is a subset of R)


### Q12

- |R| = m and |S| = n with m > n
- At least one common attribute

B. The maximum results size of natural full outer join R ⟗ S is (m × n)
  - Maximum is when it is reduced to cross product
C. The minimum result size of the cartesian product R × S is (m × n)
  - Since we have the dot notation to refer to a name, this works even if R and S share a common attribute
  - This is always m × n in size
D. The maximum results size of the natural join R ⋈ S is (m × n)
  - Maximum is when it is reduced to cross product

How to reduce natural join to cross product.
1. Common attribute is not unique.
2. Have m duplicates in R for common attributes.
3. Have n duplicates in S for common attributes.
4. Result will be (m × n).



## FD

R = { A, B, C, D, E, F }
Σ = { {C} → {A},  {B,C} → {D},  {B,F} → {C,D},  {A,B} → {F},  {E} → {B} }

### Q14 - Q20

Logically entailed:
- {A,E} → {F}
- {C,E} → {F}
- {A,D} → {A}

Keys     :  {A,E} ; {C,E} ; {E,F}
Superkeys:  {C,D,E} ; {D,E,F} ; {A,E,F}
- Find superset of Keys

Projection:  { {C,E} → {F}, {E,F} → {C} }
Functionally Equivalent: {A,B}, {B,F}
- Check the closure

BCNF Violation:  {C} → {A} ; {B,C} → {D} ; {B,F} → {C} ; {B,F} → {D} ; {A,B} → {F} ; {E} → {B}
3NF Violation :              {B,C} → {D} ;               {B,F} → {D} ;               {E} → {B}
- From keys compute prime attributes and remove violations from previous questions


### Q21

Lossless-Join
-  { {A, B, C, E}, {C, D, E, F} }
-  { {A, D, E, F}, {B, C, E, F} }



## BCNF

R = { A, B, C, D, E }
Σ = { {A,B} → {A,C},  {C,D} → {A,B},  {B,C} → {A},  {A} → {B,C} }

### Q23 - Q25

Candidate Keys: {A,D,E} ; {C,D,E}
One possible BCNF decomposition: { {A,D}, {A,B,C}, {C,D,E} }
- {C,D} → {A} is not preserved

Other answers are possible.  There is a BCNF decomposition that is dependency preserving.  Q24 is only awarded marks if Q23 is correct.



## 3NF

R = { A, B, C, D, E, F }
Σ = { {A,C} → {B,D},  {B,C} → {B,D,E},  {A,B,D} → {C},  {A,C,E} → {A,D},  {A,B} → {A,C,E},  {B,C,D} → {E} }

### Q27 - Q29

Candidate Keys : {A,B,F} ; {A,C,F}
Minimal Cover  : { {A,B} → {C},  {A,C} → {B},  {B,C} → {D},  {B,C} → {E} }
Canonical Cover: { {A,B} → {C},  {A,C} → {B},  {B,C} → {D,E} }
Construction   :   {A,B,C}    ,  {A,B,C}    ,  {B,C,D,E}
Subsumption    :   {A,B,C}    ,  {B,C,D,E}
Add Key        :   {A,B,C}    ,  {B,C,D,E}  ,  {A,B,F}



## Misc

### Q30

- R is not in BCNF because fd1 = {A,B} → {D} violates the BCNF property.
- Using fd1, we split R = {A, B, C, D} into R1 = {A,B,D} and R2 = {A,B,C}.
    - R1 is in BCNF.
    - R2 is not in BCNF because fd2 = {C} → {A} violates the BCNF property.
    - Using fd2, we split R2 into R3 = {A,C} and R4 = {B,C}.
        - R3 in in BCNF.
        - R4 is in BCNF.

