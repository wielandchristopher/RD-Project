package at.ac.fh.salzburg.smartmeter;

import at.ac.fh.salzburg.smartmeter.access.AccessMode;
import at.ac.fh.salzburg.smartmeter.access.DataSourceContext;
import at.ac.fh.salzburg.smartmeter.access.UserContext;
import at.ac.fh.salzburg.smartmeter.data.DataSourceInformation;
import at.ac.fh.salzburg.smartmeter.data.PaginatedData;
import at.ac.fh.salzburg.smartmeter.data.QueryResult;

/**
 * Interface used for Paginated data Access
 */
public interface DataAccess {

    /**
     * Access the SmartMeter data per datasource.
     *
     * due the huge amount of data it can be queried in a paginated way.
     *
     * To access entry 24 you can for example set pagesize to 10 and access the 3rd page (zero based index)
     * so page 1 contains entry 0-9, page 2 contains entry 10-19 and page 3 contains 20-29
     * the call can look like getDataPerSource(context, "myDataSource", 10, 2);
     *
     * @param userContext the context of the user requesting the data
     * @param accessMode the access mode
     * @param sourceContext the name of the source
     * @param pageSize the size of the page
     * @param page the page number
     * @return an data access object
     */
    PaginatedData getDataPerSource(UserContext userContext, AccessMode accessMode, DataSourceContext sourceContext, int pageSize, int page);

    /**
     * Access the SmartMeter data per datasource.
     *
     * due the huge amount of data it can be queried in a paginated way.
     *
     * To access entry 24 you can for example set pagesize to 10 and access the 3rd page (zero based index)
     * so page 1 contains entry 0-9, page 2 contains entry 10-19 and page 3 contains 20-29
     * the call can look like getDataPerSource(context, "myDataSource", 10, 2);
     *
     * @param userContext the context of the user requesting the data
     * @param accessMode the access mode
     * @param sourceContext the name of the source
     * @param pageSize the size of the page
     * @param page the page number
     * @param resolution the desired resolution
     * @return an data access object
     */
    PaginatedData getDataPerSource(UserContext userContext, AccessMode accessMode, DataSourceContext sourceContext, int pageSize, int page, int resolution);

    /**
     *
     * @param userContext the context of the user requesting the data
     * @param sourceName the unique name of the datasource
     * @return the information about the given datasource
     */
    QueryResult<DataSourceInformation> getDataSourceInformation(UserContext userContext, String sourceName);


    /**
     *
     * @param userContext the context of the user requesting the data
     * @param accessMode the access mode
     * @param pageSize the size of the page
     * @param page the page number
     * @param resolution the desired resolution
     * @param dataSources an array of the names of data sources
     * @return
     */
    QueryResult compareDataSources(UserContext userContext, AccessMode accessMode, int pageSize, int page, int resolution, DataSourceContext... dataSources);


}
