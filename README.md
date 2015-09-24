# PayIt Core
-----

## Mongo Migrations
This application is backed by MongoDB and unlike a typical RDMS that requires DDL migration scripts, MongoDB does not, basically the "schema" of a document is defined by code itself.  However there are things and situations that might need a more traditional migration script strategy, for example added MongoDB indexes to collections and potenitally modifying cross documents based on changes to document structures.

To create a new migration run the following SBT command;

### From Outside SBT Console
    
    sbt "migration <<name>>"

### From Within SBT Console

    sbt
    > migration <<name>>
    
In both cases the <<name> would be the name of the script that gets created, for example, AddIndexesToCustomers.

The result of the "migration" command will be a new ".scala" class that gets created in the following directory;

    src/main/scala/com/payit/data/migrations/Migrate_89789798789798_AddIndexesToCustomers
    
### Migration Script Class Structure
The newly generated migration script is a class that extends "MongoMigration".
    
The 2 methods that need to be implemented are;

    def up(db: MongoDB)
    def down(db: MongoDB)
    
As probably obvious the "up" method is run to add a new migration to an existing MongoDB DB and the "down" is used to rollback the changes added by the script.
    
MongoMigration also has some helpful methods for adding/dropping indexes.

## Testing

### Unit Testing
Unit tests, which are intended to be test that are testing functional components that do not rely on other components or outside dependencies.

Unit Test classes should live under the following directory of this project;

    src/test/scala

To run all unit tests run the following command in the SBT console;

    test

To run a single unit test specification run the following command in the SBT console;

    test-only com.payit.<<Spec Class>>

### Integration
Integration tests are intended to be tests that are testing a stack of components together or that rely on outside dependencies, such as a database.

Integration Test classes should live under the following directory of this project;

    src/it/scala

To run all integration tests run the following command in the SBT console;

    it:test

To run a single integration test specification run the following command in the SBT console;

    it:test-only com.payit.<<Spec Class>>

### Running All Tests
Running all tests, i.e. unit and integration tests together, run the following command in the SBT console;

  test-all

## MongoDB Commands
----
The following are just some helpful commands to list if you are new to MongoDB

* mongo -> start command line client
* help -> show commands
* show dbs -> show all databases
* use <<dbs>> -> switch to a DBS
* show collections -> show all collections of a DBS
* db.<<collection>>.find() -> show all data in a collection
* db.<<collection>>.getIndexes() -> show indexes of a collection