use EMP;

CREATE TABLE Cars
(
ID int NOT NULL AUTO_INCREMENT,
Date_Bought date NOT NULL,
Item_ID varchar(31) NOT NULL,
Vehicle varchar(63) NOT NULL,
Item_Cost float NOT NULL,
Title int NOT NULL,
Date_Paid date,

PRIMARY KEY (ID)
)