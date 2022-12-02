/* Sample data
 * 
INSERT INTO Roles (role_id, name) VALUES (1, 'USER');
INSERT INTO Roles (role_id, name) VALUES (2, 'ADMIN');


INSERT INTO Users ( user_id, name, username, hashed_password, salt, account_locked, city, country) 
	VALUES ( 505, 'John Smith', 'john@gmail.com', '$2a$10$78qyrUOJsjubtqkyflmdX.1mRa7s.atCD9bkSGih3sAwxs4vmuVYe', 
	'[B@583b177a', false, 'MyCity', 'MyCountry');
INSERT INTO Users ( user_id, name, username, hashed_password, salt, account_locked, city, country) 
	VALUES (506, 'Simon Lowry', 'simon@gmail.com',  'somePassword', 'salt', false, 'MyCity', 'MyCountry');
INSERT INTO Users ( user_id, name, username, hashed_password, salt, account_locked, city, country) 
	VALUES (507, 'Bart Simpson', 'bart@gmail.com',  'somepassword', 'salt', false, 'MyCity', 'MyCountry');
INSERT INTO Users ( user_id, name, username, hashed_password, salt, account_locked, city, country) 
	VALUES (508, 'Jesus Christ', 'jesus@godmail.com', 'password', 'salt', false, 'MyCity', 'MyCountry');
INSERT INTO Users ( user_id, name, username, hashed_password, salt, account_locked, city, country) 
	VALUES (509, 'Jacob Brady', 'jacob@gmail.com', 'somePassword', 'salt', false, 'MyCity', 'MyCountry');
	
INSERT INTO User_Role (user_id, role_id) VALUES (505, 1); -- user John has role USER
INSERT INTO `User_Role` (`user_id`, `role_id`) VALUES (506, 1); -- user Simon has role USER
INSERT INTO `User_Role` (`user_id`, `role_id`) VALUES (507, 1); -- user Bart has role USER
INSERT INTO `User_Role` (`user_id`, `role_id`) VALUES (508, 2); -- user Jacob has role ADMIN
*/


	
	