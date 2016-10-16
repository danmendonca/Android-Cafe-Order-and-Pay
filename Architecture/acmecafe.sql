DROP TABLE OrderVoucher
;
DROP TABLE MenuProduct
;
DROP TABLE Menu
;
DROP TABLE Voucher
;
DROP TABLE RequestLine
;
DROP TABLE Product
;
DROP TABLE OrderRequestLine
;
DROP TABLE Order
;
DROP TABLE Costumer
;



CREATE TABLE OrderVoucher ( 
	VoucherId integer NOT NULL,
	OrderId integer NOT NULL
)
;

CREATE TABLE MenuProduct ( 
	MenuId integer NOT NULL,
	ProductId integer NOT NULL
)
;

CREATE TABLE Menu ( 
	MenuId integer NOT NULL,
	Name text,
	Active boolean NOT NULL
)
;

CREATE TABLE Voucher ( 
	VoucherId integer NOT NULL,
	Key text NOT NULL,
	Number integer NOT NULL,
	Type bytea NOT NULL,
	CostumerId integer NOT NULL,
	IsUsed boolean NOT NULL
)
;

CREATE TABLE RequestLine ( 
	RequestLineId integer NOT NULL,
	ProductId integer NOT NULL,
	Quantity integer NOT NULL,
	UnitPrice real NOT NULL
)
;

CREATE TABLE Product ( 
	ProductId integer NOT NULL,
	Name text NOT NULL
)
;

CREATE TABLE OrderRequestLine ( 
	OrderId integer NOT NULL,
	RequestLineId integer NOT NULL
)
;

CREATE TABLE Order ( 
	OrderId integer NOT NULL,
	CostumerId integer NOT NULL
)
;

CREATE TABLE Costumer ( 
	CostumerId integer NOT NULL,
	Name text NOT NULL,
	Pin text NOT NULL,
	CreditCard text NOT NULL,
	Username text NOT NULL,
	Password text NOT NULL
)
;


ALTER TABLE OrderVoucher ADD CONSTRAINT PK_OrderVoucher 
	PRIMARY KEY (VoucherId, OrderId)
;


ALTER TABLE MenuProduct ADD CONSTRAINT PK_MenuProduct 
	PRIMARY KEY (MenuId, ProductId)
;


ALTER TABLE Menu ADD CONSTRAINT PK_Menu 
	PRIMARY KEY (MenuId)
;


ALTER TABLE Voucher ADD CONSTRAINT PK_Voucher 
	PRIMARY KEY (VoucherId)
;


ALTER TABLE RequestLine ADD CONSTRAINT PK_RequestLine 
	PRIMARY KEY (RequestLineId)
;


ALTER TABLE Product ADD CONSTRAINT PK_Product 
	PRIMARY KEY (ProductId)
;


ALTER TABLE OrderRequestLine ADD CONSTRAINT PK_OrderRequestLine 
	PRIMARY KEY (OrderId, RequestLineId)
;


ALTER TABLE Order ADD CONSTRAINT PK_Order 
	PRIMARY KEY (OrderId)
;


ALTER TABLE Costumer ADD CONSTRAINT PK_Costumer 
	PRIMARY KEY (CostumerId)
;



ALTER TABLE Voucher
	ADD CONSTRAINT UQ_Voucher_Key UNIQUE (Key)
;
ALTER TABLE Voucher
	ADD CONSTRAINT UQ_Voucher_Number UNIQUE (Number)
;
ALTER TABLE Product
	ADD CONSTRAINT UQ_Product_Name UNIQUE (Name)
;
ALTER TABLE Costumer
	ADD CONSTRAINT UQ_Costumer_Username UNIQUE (Username)
;


ALTER TABLE OrderVoucher ADD CONSTRAINT FK_OrderVoucher_Order 
	FOREIGN KEY (OrderId) REFERENCES Order (OrderId)
ON DELETE CASCADE
;

ALTER TABLE OrderVoucher ADD CONSTRAINT FK_OrderVoucher_Voucher 
	FOREIGN KEY (VoucherId) REFERENCES Voucher (VoucherId)
ON DELETE CASCADE
;

ALTER TABLE MenuProduct ADD CONSTRAINT FK_MenuProduct_Menu 
	FOREIGN KEY (MenuId) REFERENCES Menu (MenuId)
ON DELETE CASCADE
;

ALTER TABLE MenuProduct ADD CONSTRAINT FK_MenuProduct_Product 
	FOREIGN KEY (ProductId) REFERENCES Product (ProductId)
ON DELETE CASCADE
;

ALTER TABLE Voucher ADD CONSTRAINT FK_Voucher_Costumer 
	FOREIGN KEY (CostumerId) REFERENCES Costumer (CostumerId)
ON DELETE CASCADE
;

ALTER TABLE RequestLine ADD CONSTRAINT FK_RequestLine_Product 
	FOREIGN KEY (ProductId) REFERENCES Product (ProductId)
ON DELETE CASCADE
;

ALTER TABLE OrderRequestLine ADD CONSTRAINT FK_OrderRequestLine_Order 
	FOREIGN KEY (OrderId) REFERENCES Order (OrderId)
ON DELETE CASCADE
;

ALTER TABLE OrderRequestLine ADD CONSTRAINT FK_OrderRequestLine_RequestLine 
	FOREIGN KEY (RequestLineId) REFERENCES RequestLine (RequestLineId)
ON DELETE CASCADE
;

ALTER TABLE Order ADD CONSTRAINT FK_Order_Costumer 
	FOREIGN KEY (CostumerId) REFERENCES Costumer (CostumerId)
ON DELETE CASCADE
;
