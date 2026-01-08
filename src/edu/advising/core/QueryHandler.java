package edu.advising.core;

import java.sql.ResultSet;
import java.sql.SQLException;

// Functional Interfaces allowing me to pass a Lambda handler into the DatabaseManager
// so the DatabaseManager will handle the connection open/close, and I can still handle
// the data/ResultSet without worrying about the connection pool or database boilerplate.
@FunctionalInterface
public interface QueryHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}
