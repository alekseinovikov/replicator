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


Solution description:
Similar problems are solved with triggers (in one DB) or with internal DB mechanisms (replication).
So, I've implemented my solution using 2 additional columns for both tables: sync_uuid and updated_at:
The first column is identifier that helps to determine if the row has been already replicated before and helps to find match in the other table.
The second column is just a timestamp that updates automatically on insert and update a row. It helps to find which row contains the most actual data.