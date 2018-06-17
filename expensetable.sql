DROP DATABASE IF EXISTS expensedb;
CREATE DATABASE expensedb;
USE expensedb;

CREATE TABLE expense( 
	id int NOT NULL AUTO_INCREMENT,
    e_value float NOT NULL,
    e_date date NOT NULL,
    e_reason varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO expense (e_value, e_date, e_reason) VALUES (12,'2008-6-5',32);
INSERT INTO expense (e_value, e_date, e_reason) VALUES (12,'2002-3-1',32);
INSERT INTO expense (e_value, e_date, e_reason) VALUES (21,'2005-6-7',21);

SELECT * FROM expense
ORDER BY id DESC;