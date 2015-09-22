# PayIT Core
-----


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