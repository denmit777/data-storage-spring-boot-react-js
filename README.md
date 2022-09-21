Task

Create a program powered by Spring. You should implement data storage with interface with single public method:
String query(String queryString)
This method allows us to perform SQL-like requests. It should support next operations:
CREATE TABLE table_name (
column1, column2, column3, ....
);

INSERT INTO table_name (column1, column2, column3, ...)
VALUES (value1, value2, value3, ...);

SELECT column1, column2, ... FROM table_name;

DELETE FROM table_name WHERE condition;

This storage supports only String types. 

Initially the program should initialize one table ‘city’ with fields (id, name, countryCode, district, population).
Please provide CRUD operations to this table with Rest API. 

1.Name of project: data-storage-spring-boot-react

2.Launch of frontend part from terminal:
node modules: data-storage-spring-boot-react\src\frontend-react\java-learn-app-main> npm install frontend part:
data-storage-spring-boot-react\src\frontend-react\java-learn-app-main> npm start

3.Ports of the project:
backend: http://localhost:8081
frontend: http://localhost:3000

4.Start page: http://localhost:3000

5.Rest controller: CityController

createTable(POST): http://localhost:8081 + body; save(POST): http://localhost:8081/cityInfo + body; getById(
GET): http://localhost:8081/cityInfo?queryString= + parameter getAll(GET): http://localhost:8081/cities?queryString= +
parameter update(PUT): http://localhost:8081/cityInfo + body; delete(DELETE): http://localhost:8081/cities?queryString=
+ parameter

6.Frontend:

http://localhost:3000 - Build query Page for creating table example:
CREATE TABLE city (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), country_code VARCHAR(50), district VARCHAR(
50), population BIGINT)";

http://localhost:3000/cityInfo - Query for insert, update and get City Page for inserting, update and searching object
by id examples:
INSERT INTO city (name, countryСode, district, population) VALUES (York, 1998, 123, 100000); SELECT * FROM city WHERE id
= 2; UPDATE city SET name = 'York1', countryCode = '1', population = '12000' WHERE id = 3;

http://localhost:3000/cities - Select and delete query Page for searching and removing objects examples:
SELECT * FROM city; SELECT * FROM city WHERE name = 'York' AND district = '123' AND population = '100000'; select id,
name, population from city where name = 'York' and district = '123' and population = '100000; DELETE FROM city WHERE id
= 3;


