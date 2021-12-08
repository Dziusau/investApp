USE investdb;

CREATE TABLE Пользователь (
	UserID INT PRIMARY KEY AUTO_INCREMENT,
	UserName VARCHAR(50) NOT NULL,
	UserPassword VARCHAR(20) NOT NULL,
	Email VARCHAR(20) DEFAULT '@gmail.com' UNIQUE NOT NULL,
	Balance BIGINT NULL,
	
	CHECK (Email != '')
);

CREATE TABLE Админ (
	AdminID INT PRIMARY KEY AUTO_INCREMENT,
	AdminName VARCHAR(50) NOT NULL,
	AdminPassword VARCHAR(20) NOT NULL
);

CREATE TABLE Актив (
	ActiveID INT PRIMARY KEY AUTO_INCREMENT,
	ActiveType VARCHAR(10) DEFAULT 'акция',
	ActiveName VARCHAR(20) NOT NULL,
	
	CHECK (ActiveType IN ('акция', 'облигация', 'ETF', 'фьючерс'))
);
ALTER TABLE Актив
ADD ActiveAccess VARCHAR(20) DEFAULT 'доступен' NOT NULL,
ADD CHECK (ActiveAccess IN ('доступен', 'не доступен'));

CREATE TABLE Котировка (
	ID INT PRIMARY KEY AUTO_INCREMENT,
	ActiveID INT NULL,
	QuotationDate DATETIME NOT NULL,
	Price BIGINT DEFAULT 0 NULL,
	
	CONSTRAINT Котировка_Актив_fk FOREIGN KEY (ActiveID) 
		REFERENCES Актив (ActiveID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Операция (
	OperationID INT PRIMARY KEY AUTO_INCREMENT,
	ActiveID INT NULL,
	UserID INT NULL,
	Amount INT NOT NULL,
	OperationDate DATETIME NOT NULL,
	OperationType VARCHAR(10) DEFAULT 'покупка',
	IsSuccess VARCHAR(10) DEFAULT 'успешно',

	CHECK (OperationType IN ('покупка', 'продажа')),
	CHECK (IsSuccess IN ('успешно', 'отклонено')),
	
	CONSTRAINT Операция_Актив_fk FOREIGN KEY (ActiveID)
		REFERENCES Актив (ActiveID) ON UPDATE SET NULL ON DELETE SET NULL,
	CONSTRAINT Операция_Пользователь_fk FOREIGN KEY (UserID)
		REFERENCES Пользователь (UserID) ON UPDATE SET NULL ON DELETE SET NULL
);

CREATE TABLE Портфель (
	UserID INT NOT NULL,
	OperationID INT NOT NULL,

	PRIMARY KEY (UserID, OperationID),

	CONSTRAINT Портфель_Пользователь_fk FOREIGN KEY (UserID)
		REFERENCES Пользователь (UserID) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT Портфель_Операция_fk FOREIGN KEY (OperationID)
		REFERENCES Операция (OperationID) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE Уведомление (
	UserID INT NOT NULL,
    AdminID INT NOT NULL,
    Message Text,
    
    CONSTRAINT Уведомление_Пользователь_fk FOREIGN KEY (UserID)
		REFERENCES Пользователь (UserID) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT Уведомление_Админ_fk FOREIGN KEY (AdminID)
		REFERENCES Админ (AdminID) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT Актив(ActiveType, ActiveName, ActiveAccess) 
VALUES 
('акция', 'Amazon', 'доступен'),
('акция', 'Apple', 'не доступен'),
('облигация', 'Сбер', 'доступен'),
('облигация', 'Гособлигация Америки', 'доступен'),
('ETF', 'SPDR S&P 500', 'доступен'),
('ETF', 'FinEx', 'не доступен'),
('фьючерс', 'UK Gilt', 'не доступен'),
('фьючерс', 'US 30Y T-Bond', 'доступен');

INSERT Котировка(ActiveID, QuotationDate, Price) 
VALUES 
(9, '2021-12-04 13:00:00', 456),
(9, '2021-12-04 16:00:00', 444),
(10, '2021-12-04 13:00:00', 567),
(10, '2021-12-04 16:00:00', 578),
(11, '2021-12-04 13:00:00', 111),
(11, '2021-12-04 16:00:00', 109),
(12, '2021-12-04 13:00:00', 134),
(12, '2021-12-04 16:00:00', 130),
(13, '2021-12-04 13:00:00', 345),
(13, '2021-12-04 16:00:00', 333),
(14, '2021-12-04 13:00:00', 234),
(14, '2021-12-04 16:00:00', 235),
(15, '2021-12-04 13:00:00', 152),
(15, '2021-12-04 16:00:00', 135),
(16, '2021-12-04 13:00:00', 243),
(16, '2021-12-04 16:00:00', 124);

