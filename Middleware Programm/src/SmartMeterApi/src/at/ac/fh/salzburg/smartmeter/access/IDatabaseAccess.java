package at.ac.fh.salzburg.smartmeter.access;

import at.ac.fh.salzburg.smartmeter.data.QueryResult;

public interface IDatabaseAccess {
    public QueryResult QueryDatabase(QueryBase query);
}
