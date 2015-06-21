use EMP;

CREATE TABLE Transactions
(
ID int NOT NULL AUTO_INCREMENT,
Last_Name varchar(63) NOT NULL,
First_Name varchar(63) NOT NULL,
Address  varchar(63),
Phone_Number  varchar(63),
Number_of_Payments int NOT NULL,
Amount_of_Payments float NOT NULL,
Payment_Frequency int NOT NULL,
Final_Payment float NOT NULL,
Total_of_Payments float	NULL,

PRIMARY KEY (ID)
)