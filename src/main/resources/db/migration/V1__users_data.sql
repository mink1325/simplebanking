insert into USERS (USER_ID, USERNAME, EMAIL, DATE_CREATED, DATE_CHANGED)
values (1, 'john', 'john@goodmail.com', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into ACCOUNTS (ACCOUNT_NO, USER_ID, DATE_CREATED, DATE_CHANGED)
values ('LT123456789012345678', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());