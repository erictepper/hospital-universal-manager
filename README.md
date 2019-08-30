# Hospital Universal Manager

### General Info
An application designed to manage aspects of day-to-day operations in a hypothetical hospital, including patient and 
staff information record-keeping, medical record-keeping, service/appointment booking, and transactional book-keeping.

This project is a revitalized version of a project done for course CPSC 304 (Relational Databases) at The University 
of British Columbia. 

Application created in collaboration with [Cecilia Mesquita](https://github.com/ceci96p). 

### Versions
**Version 0:** Created application from-scratch during a University course, using university servers to host the database and Oracle 
as the relational database management system. 

**Version 0.1:** Re-hosted application on the Heroku PaaS and used PostgreSQL as the relational database management 
system, since the previously-used university server was not accessible after the course was over. Installed the 
PostgreSQL driver in order to access the database. 

**Version 0.2:** 
* **0.2.1:** Removed the HerokuLogin class/view and connected to the Heroku database directly from the Login class. 

### To-do
###### Features to Add
1. Remove HerokuLogin class and connect directly to the database from Login class. 

###### Bugs to Fix
1. Fix "Patients scheduled for all services" query under the StaffView. 
2. Fix StringIndexOutOfBoundsException when no columns are selected under StaffView.searchRecords().
