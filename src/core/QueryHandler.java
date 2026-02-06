package core;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
interface QueryHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}
