CREATE TABLE books(
       id VARCHAR(14) PRIMARY KEY,
       title VARCHAR(128) NOT NULL,
       author VARCHAR(128),
       thumbnail VARCHAR(1024),
       read BOOLEAN DEFAULT FALSE,
       created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);