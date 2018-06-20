Task:
We need to create 2 database tables called Task_definition and Task_definition_mirror. Each table has the following columns: [ID, Name, Description]. ID is a Primary Key. Name can’t be null, Description can.
The users can modify data in any of these tables at any given point of time(i.e. change the value of Name or/and Description, delete rows, insert new rows).
Once any change takes place on any of the tables, the change should be synced to the other table.
The users or/and other applications can modify tables directly (i.e. no web application, just SQL statements are run on DB).
No DB triggers are allowed. The solution should be written in pure Java.

Hint: you can leverage Spring-boot-scheduler features

Requirements:
·        The solution should work
·        Clean and readable code
·        Project should use maven
·        Project should use Sprint Boot
·        Flyway should manage the creation of table definitions
·        Tests should be included. Tests should prove your solution works.
·        Please use H2 database

Remarks:
* Nothing was said about AUTO_INCREMENT on primary key

