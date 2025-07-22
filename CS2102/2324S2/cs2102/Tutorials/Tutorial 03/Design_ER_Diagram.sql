-- WITHOUT AGGREGATE

/*
Entity set member will get translated into a table member
with columns name, addreess, and card_number.

Here, card_number is the primary key.
*/
CREATE TABLE IF NOT EXISTS member (
  card_number  CHAR(10)     PRIMARY KEY,
	address      VARCHAR(64)  NOT NULL,
	name         VARCHAR(32)  NOT NULL
);


/*
Entity set wine will get translated to table wine with the composite primary key
appellation, name & vintage.

Other columns can be manufacturer, country_of_origin, etc.
*/
CREATE TABLE IF NOT EXISTS wine (
  name            VARCHAR(32),
	appellation     VARCHAR(32),
	vintage         DATE,
	alcohol_degree  NUMERIC NOT NULL,
	bottled         VARCHAR(128) NOT NULL,
	certification   VARCHAR(64),
	country         VARCHAR(32) NOT NULL,
	PRIMARY KEY (name, appellation, vintage)
);


/*
Exception 2: (1, 1) Participation Constraints
Exception 3: Weak Entity

Due to exception on (1, 1) Participation Constraints and Weak Entity constraint,
entity set "bottle" and relationship set "contain" will be merged
together in one table say bottle_contain_in.

Here, the primary key of the table is the composition of the key of the entity set
which is bottle_number with the (composite) key of the dominant set.
which are appellation, name and vintage referenced from table wine.
*/
CREATE TABLE IF NOT EXISTS bottle (
  wine_name       VARCHAR(32),
	appellation     VARCHAR(32),
	vintage         DATE,
	number          INTEGER CHECK (number > 0),
	PRIMARY KEY (number, wine_name, appellation, vintage),
	FOREIGN KEY (wine_name, appellation, vintage)
	  REFERENCES wine (name, appellation, vintage)
);


/*
Relationship set taste will get translated to table taste with columns card_number
referenced from table member, column bottle_number referenced from table bottle_contain.

Additional columns are tasting_date and rating.
*/
CREATE TABLE IF NOT EXISTS tasted (
  wine_name      VARCHAR(32),
	appellation    VARCHAR(32),
	vintage        DATE,
	bottle_number  INTEGER,
	member         CHAR(10),
  tasting_date   DATE NOT NULL,
  rating         VARCHAR(32) NOT NULL,
	  REFERENCES member (card_number),
	PRIMARY KEY (member, number, wine_name, appellation, vintage),
	FOREIGN KEY (number, wine_name, appellation, vintage)
	  REFERENCES bottle (number, wine_name, appellation, vintage)
);




-- WITH AGGREGATE

/*
Entity set member will get translated into a table member
with columns name, addreess, and card_number.

Here, card_number is the primary key.
*/
CREATE TABLE IF NOT EXISTS member (
  card_number  CHAR(10)     PRIMARY KEY,
	address      VARCHAR(64)  NOT NULL,
	name         VARCHAR(32)  NOT NULL
);


/*
Entity set wine will get translated to table wine with the composite primary key
appellation, name & vintage.

Other columns can be manufacturer, country_of_origin, etc.
*/
CREATE TABLE IF NOT EXISTS wine (
  name            VARCHAR(32),
	appellation     VARCHAR(32),
	vintage         DATE,
	alcohol_degree  NUMERIC NOT NULL,
	bottled         VARCHAR(128) NOT NULL,
	certification   VARCHAR(64),
	country         VARCHAR(32) NOT NULL,
	PRIMARY KEY (name, appellation, vintage)
);


/*
Entity set session will get translated to table session with the composite primary key
year and week.
*/
CREATE TABLE session (
	year  INTEGER,
	week  INTEGER,
	PRIMARY KEY (year, week)
);


/*
Exception 2: (1, 1) Participation Constraints
Exception 3: Weak Entity

Due to exception on (1, 1) Participation Constraints and Weak Entity constraint,
entity set "bottle" and relationship set "contain" will be merged
together in one table say bottle_contain_in.

Here, the primary key of the table is the composition of the key of the entity set
which is bottle_number with the (composite) key of the dominant set.
which are appellation, name and vintage referenced from table wine.
*/
CREATE TABLE IF NOT EXISTS bottle (
  wine_name       VARCHAR(32),
	appellation     VARCHAR(32),
	vintage         DATE,
	number          INTEGER CHECK (number > 0),
	PRIMARY KEY (number, wine_name, appellation, vintage),
	FOREIGN KEY (wine_name, appellation, vintage)
	  REFERENCES wine (name, appellation, vintage)
);


/*
Exception 1: (0, 1) Participation Constraints

Due to exception on (0, 1) Participation Constraints the primary key is only
from the entity set bottle.

Hence, session year and week is not part of the primary key.

This is also an aggregate.  An aggregate is first and foremost a relationship set.
So it is created as if it is a relationship set.  It can the be used as if it is
an entity set.  But the primary keys are formed based on the rule for relationship
set.
*/
CREATE TABLE IF NOT EXISTS opened (
	wine_name       VARCHAR(32),
	appellation     VARCHAR(32),
	vintage         DATE,
	bottle_number   INTEGER,
	session_year    INTEGER NOT NULL,
	session_week    INTEGER NOT NULL,
	PRIMARY KEY (bottle_number, wine_name, appellation, vintage),
	FOREIGN KEY (session_year, session_week)
	  REFERENCES session (year, week),
	FOREIGN KEY (bottle_number, wine_name, appellation, vintage)
		REFERENCES bottle (number, wine_name, appellation, vintage)
);


/*
Relationship set taste will get translated to table taste with columns card_number
referenced from table member, column bottle_number referenced from table bottle_contain.

Additional column is only rating.  There is no need for tasting_date as it is already
handled by the session through the aggregate opened.
*/
CREATE TABLE tasted (
  wine_name      VARCHAR(32),
	appellation    VARCHAR(32),
	vintage        DATE,
	bottle_number  INTEGER,
	member         CHAR(10),
  rating         VARCHAR(32) NOT NULL,
	  REFERENCES member (card_number),
	PRIMARY KEY (member, bottle_number, wine_name, appellation, vintage),
	FOREIGN KEY (bottle_number, wine_name, appellation, vintage)
	  REFERENCES opened (bottle_number, wine_name, appellation, vintage)
);