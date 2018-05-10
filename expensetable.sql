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


