Car Renting System 
Description
This system allows users’ interactive operation through utilizing the Java JDBC API based on the selected operator. In total, the system will create 7 table schemas for data storage in the database.

Input datafiles (ASCII-encoded Linux text file):
user categories.txt
users.txt
car categories.txt
cars.txt
renting records.txt

Compile Java Classes
- Main.java: Creating a menu for choosing different operators 

- Admin.java: 
* Creating a menu for choosing different admin operations
* Creating table schemas in the database
* Deleting table schemas in the database
* Inputting data from users-specified dataset
* Showing the number of records in each table schema

Functions deployment:
The creation and deletion of the schemas are done by direct input of SQL statement. After specifying the folder path, folders with corresponding filenames will be read and insert into the schemas. In counting the records number, array is created in storing the count of each table by the COUNT(*) function. Finally, the administrator manual is performed after operation.

- User.java:
* Creating a menu for choosing different user operations
* Searching for cars by call number, car name or company
* Showing all renting record user in descending order of renting date

Functions deployment:
To start with, all the operations are performed by prepared statement as string comparison from users’ input is always performed. The user chooses to search cars from three ways after choosing the operation preference. Expect searching by the call number will show cars with exact matching, the system will show the detail of all cars which is partial matched with keywords and sorted in ascending order on call number. For example, cars named “Car12” will be considered with search keyword “Car1” in car name. The result of all user function is in table format and ends with sentence “End of Query”. To perform this function, the deduction of number of matched cars copies and the count of respective occupied car are needed so array is created in storing the number of occupied cars which matched the search keywords. The user manual is performed after query.

- Manager.java:
* Creating a menu for choosing different manger operations
* Renting a car copy by inputting call number, copy number and user ID
* Returning a car by inputting call number, copy number 
* Listing all un-returned car copies which are check-out within a period
* Outputting an error message 

Function deployment:
All operations are completed by prepared statement and a successful message will be printed when the operation is completed. Like the renting operation, the return operation is also deployed by prepared statement while performing one more step in updating the return date by the current date. The manger can also list all un-returned car copies with checkout date by manger-inserted period that is performed through BETWEEN function in SQL. Error message will be return if the operation is failed in the manger function. For error handling, the output an information message in layman terms and in the next line of the result. There are 7 types of error message in total, covering different input and output error in operations.

Q&A
Q: How should I do while choosing a wrong operator’s operation?
A: Inputting an error input will lead the user to the main menu.
