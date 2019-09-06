# Hospital Universal Manager

### General Info
Version 1.0.0

An application designed to manage aspects of day-to-day operations in a hypothetical hospital, including patient and 
staff information record-keeping, medical record-keeping, service/appointment booking, and transactional book-keeping. 
Class code can be found in [src/main/java](master/src/main/java).

This project is a revitalized version of a project done for course CPSC 304 (Relational Databases) at The University 
of British Columbia. 

Application created in collaboration with [Cecilia Mesquita](https://github.com/ceci96p). 

### Versions
**Version 0:** Created application from-scratch during a University course, using a university server to host the 
database and Oracle as the relational database management system. 

**Version 0.1:** Re-hosted application on the Salesforce Heroku PaaS and used PostgreSQL as the relational database 
management system, since the previously-used university server was not accessible after the course was over. Installed 
the PostgreSQL driver in order to access the database. 

**Version 0.2:**  Bug fixes, added logout button to views, and removed now-unnecessary class. 
* **0.2.1:** Removed the HerokuLogin class/view and connected to the Heroku database directly from the Login class. 
* **0.2.2:** Added logout button for all the user types, so one does not have to close the application to re-login.
* **0.2.3:** Fixed PreparedStatements to prevent [malicious SQL injection](https://xkcd.com/327/), and fixed bug where 
Service booking button was not displayed in the RecepView. 
* **0.2.4:** RecepView and StaffView now correctly handle the case where no options are selected in searchRecords(). 
* **0.2.5:** All opened SQL statements now get closed after use. 

**Version 1.0.0:** Packaged and deployed the fully functional Create-Read-Update-Delete (C.R.U.D.) hospital management 
app in an executable .jar which can be downloaded and run on any system that has at least Java 8 installed. 

### To-do
###### Features to Add
1. Create download/use information. 
2. Change log-in interface. 
3. Use regular expressions to evaluate whether user is Patient, Staff, or Receptionist (rather than 
using a drop-down box) on the log-in view. 
4. Use regular expressions to check input for update queries. 

###### Bugs to Fix
* No current known bugs.
