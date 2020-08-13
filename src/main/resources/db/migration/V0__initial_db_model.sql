CREATE TABLE USERS (
    USER_ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    USERNAME varchar(20) NOT NULL,
    PASSWORD varchar(256) NOT NULL,
    EMAIL varchar(50) NOT NULL,
    DATE_CREATED timestamp NOT NULL,
    DATE_CHANGED timestamp NOT NULL
);
CREATE TABLE ACCOUNTS (
    ACCOUNT_ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ACCOUNT_NO varchar(20) NOT NULL,
    USER_ID bigint NOT NULL,
    DATE_CREATED timestamp NOT NULL,
    DATE_CHANGED timestamp NOT NULL,
    foreign key (USER_ID) references USERS(USER_ID)
);
CREATE TABLE ACCOUNTS_OPERATIONS (
    OPERATION_ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    OPERATION_TYPE int NOT NULL,
    ACCOUNT_ID bigint NOT NULL,
    AMOUNT DECIMAL NOT NULL,
    DATE_CREATED timestamp NOT NULL,
    foreign key (ACCOUNT_ID) references ACCOUNTS(ACCOUNT_ID)
);