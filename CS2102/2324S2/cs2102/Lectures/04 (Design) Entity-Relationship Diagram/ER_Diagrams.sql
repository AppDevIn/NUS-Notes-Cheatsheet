CREATE TABLE company (
  name VARCHAR(64) PRIMARY KEY,
  address VARCHAR(128)
);

CREATE TABLE person (
  first_name VARCHAR(32),
  last_name VARCHAR(32),
  address VARCHAR(128) NOT NULL,
  PRIMARY KEY (first_name, last_name)
);

CREATE TABLE student (
  email VARCHAR(64) PRIMARY KEY,
  domain VARCHAR(12) NOT NULL,
  userid VARCHAR(50) NOT NULL,
  UNIQUE (domain, userid)
);

CREATE TABLE contract(
  start DATE NOT NULL,
  end DATE NOT NULL,
  first_name VARCHAR(32),
  last_name VARCHAR(32),
  name VARCHAR(64),
  PRIMARY KEY (first_name, last_name, name),
  FOREIGN KEY (first_name , last_name)
    REFERENCES person(first_name, last_name),
  FOREIGN KEY (name) REFERENCES company(name)
);

CREATE TABLE work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8),
  cname VARCHAR(32),
  PRIMARY KEY (enumber, cname),
  FOREIGN KEY (enumber)
    REFERENCES employee(number),
  FOREIGN KEY (cname)
    REFERENCES company(name)
);

CREATE TABLE work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8) PRIMARY KEY,
  cname VARCHAR(32),

  FOREIGN KEY (enumber)
    REFERENCES employee(number),
  FOREIGN KEY (cname)
    REFERENCES company(name)
);

CREATE TABLE work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8) PRIMARY KEY,
  cname VARCHAR(32),

  FOREIGN KEY (enumber)
    REFERENCES employee(number),
  FOREIGN KEY (cname)
    REFERENCES company(name)
);

CREATE TABLE employee_work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8) PRIMARY KEY,
  ename CHAR(32) NOT NULL,
  cname VARCHAR(32) NOT NULL,
  FOREIGN KEY (cname)
    REFERENCES company(name)
);

CREATE TABLE employee_work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8) PRIMARY KEY,
  ename CHAR(32) NOT NULL,
  cname VARCHAR(32) NOT NULL,

  FOREIGN KEY (cname)
    REFERENCES company(name)
);

CREATE TABLE employee_work_for (
  start DATE NOT NULL,
  end DATE NOT NULL,
  enumber CHAR(8),
  ename CHAR(32) NOT NULL,
  cname VARCHAR(32) NOT NULL,
  PRIMARY KEY (enumber, cname),
  FOREIGN KEY (cname)
    REFERENCES company(name)
);
