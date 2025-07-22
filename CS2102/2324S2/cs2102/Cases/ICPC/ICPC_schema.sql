CREATE TABLE IF NOT EXISTS region (
  name  VARCHAR(32) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS site (
  name    VARCHAR(32) PRIMARY KEY,
  region  VARCHAR(32) NOT NULL
    REFERENCES region (name)
);

CREATE TABLE IF NOT EXISTS contest (
  site    VARCHAR(32)
    REFERENCES site (name),
  year    INT
    CHECK (year > 2000),
  name    VARCHAR(256)  NOT NULL,
  date    DATE  NOT NULL,
  PRIMARY KEY (site, year)
);

CREATE TABLE IF NOT EXISTS question (
  name    VARCHAR(5),
  site    VARCHAR(32),
  year    INT,
  FOREIGN KEY (site, year)
    REFERENCES contest (site, year),
  PRIMARY KEY (name, site, year)
);

CREATE TABLE IF NOT EXISTS university (
  name    VARCHAR(256)  PRIMARY KEY,
  region  VARCHAR(32)   NOT NULL
    REFERENCES region (name)
);

CREATE TABLE IF NOT EXISTS team (
  name    VARCHAR(256)  PRIMARY KEY,
  univ    VARCHAR(256)  NOT NULL
    REFERENCES university (name)
);

CREATE TABLE IF NOT EXISTS participate (
  team    VARCHAR(256)
    REFERENCES team (name),
  site    VARCHAR(32),
  year    INT,
  solve   INT NOT NULL CHECK (solve >= 0),
  time    INT NOT NULL CHECK (time >= 0),
  last    INT NOT NULL CHECK (last >= 0 AND last <= time),
  FOREIGN KEY (site, year)
    REFERENCES contest (site, year),
  PRIMARY KEY (team, site, year)
);