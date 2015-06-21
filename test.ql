use EMP;

CREATE TABLE Persons
(
ID int NOT NULL AUTO_INCREMENT,
LastName varchar(127) NOT NULL,
FirstName varchar(127) NOT NULL,
email  varchar(127),
phone  varchar(63),
PRIMARY KEY (ID)
)