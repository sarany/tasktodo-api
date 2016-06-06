--DROP TABLE tasks IF EXISTS;

CREATE TABLE tasks (
	id 			INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  	subject     VARCHAR(50),
  	description VARCHAR(100),
  	status  	VARCHAR(20)
);