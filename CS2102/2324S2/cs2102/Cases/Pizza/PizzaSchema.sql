CREATE TABLE customer (
  cname TEXT,
  area TEXT
);

CREATE TABLE restaurant (
  rname TEXT,
  area TEXT
);

CREATE TABLE pizza (
  pizza TEXT
);

CREATE TABLE likes (
  cname TEXT,
  pizza TEXT
);

CREATE TABLE sells (
  rname TEXT,
  pizza TEXT,
  price INTEGER
);
