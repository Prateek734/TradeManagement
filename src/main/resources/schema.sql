DROP TABLE IF EXISTS trade;
CREATE TABLE trade( 
   id INT AUTO_INCREMENT  PRIMARY KEY,
   tradeid VARCHAR(50) NOT NULL, 
   version int NOT NULL, 
   counterpartyid VARCHAR(20) NOT NULL, 
   bookid VARCHAR(50) NOT NULL,
   maturitydate VARCHAR(50) NOT NULL,
   createddate VARCHAR(50) NOT NULL,
   isexpired VARCHAR(50) NOT NULL
);