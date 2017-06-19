package at.ac.fh.salzburg.smartmeter.rest;

import at.ac.fh.salzburg.smartmeter.access.IDataSourceContext;
import at.ac.fh.salzburg.smartmeter.access.IUserContext;
import at.ac.fh.salzburg.smartmeter.access.QueryBase;
import at.ac.fh.salzburg.smartmeter.data.data.QueryResult;
import at.ac.fh.salzburg.smartmeter.data.data.StringListQueryResult;
import at.ac.fh.salzburg.smartmeter.access.QueryStatusCode;

import at.ac.fh.salzburg.smartmeter.data.entities.MeterDataEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 * Created by maximilian on 01.06.2017.
 */
@Controller
public class GetAdjustedMeterValueVectorsforPeriodController extends CustomQueryControllerBase {


    //public class GetAdjustedMeterValueVectorsforPeriodController extends CustomQueryControllerBase {
    @RequestMapping("/query/adjustedmeterdatavectors")
    @ResponseBody

    //int meterId1 = 0;
    //int meterId2 = 0;

    public QueryResult<?> GetAdjustedMeterValueVectorsforPeriod(@RequestParam(value = "meterId1") String pmeterId1,
                                                                @RequestParam(value = "meterId2") String pmeterId2,
                                                                @RequestParam(value = "tspvon") String ptspvon,
                                                                @RequestParam(value = "tspbis") String ptspbis) {
        return databaseAccess.QueryDatabase(new GetAdjustedMeterValueVectorsforPeriodController.CustomMeterQuery(null, null, pmeterId1, pmeterId2, ptspvon, ptspbis));
    }

    private class CustomMeterQuery extends QueryBase<List<MeterDataEntity>> {

        int _meterId1 = 0;
        int _meterId2 = 0;
        String _tspvon = "";
        String _tspbis = "";

        public CustomMeterQuery(IUserContext userContext, IDataSourceContext dataSourceContext, String pmeterId1, String pmeterId2, String ptspvon, String ptspbis) {
            super(userContext, dataSourceContext);
            _meterId1 = Integer.parseInt(pmeterId1);
            _meterId2 = Integer.parseInt(pmeterId2);
            _tspvon = ptspvon;
            _tspbis = ptspbis;
        }

        @Override
        public String getQuery() {

            // blöden Timestamp zerfledern
            long von = extractMilisfromTsp(_tspvon);
            long bis = extractMilisfromTsp(_tspbis);

            StringBuilder querystring = new StringBuilder("select data_id, meter_id, timestamp, count_total, count_register1, count_register2, count_register3, count_register4, power_p1, power_p2, power_p3, work_p1, work_p2, work_p3, frequency, voltage from meter_data where meter_id in (");
            querystring.append(Integer.toString(_meterId1));
            querystring.append(", ");
            querystring.append(Integer.toString(_meterId2));
            querystring.append(") and timestamp between \"");
            //querystring.append(Long.toString(von));
            querystring.append(_tspvon);
            querystring.append("\" and \"");

            //querystring.append(Long.toString(bis));
            querystring.append(_tspbis);

            querystring.append("\" order by meter_id, data_id;");

            return querystring.toString();
        }

        @Override
        public QueryResult<List<MeterDataEntity>> parseDatabaseResultSet(ResultSet resultSet) {
            My2vectorsQueryResult result = new My2vectorsQueryResult();
            try {
                while (resultSet.next()) {
                    //System.out.println("Sätze:" + ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
                    MeterDataEntity c = new MeterDataEntity();
                    c.setDataId(resultSet.getInt("data_id"));
                    c.setMeterId(resultSet.getInt("meter_id"));
                    c.setTimestamp(resultSet.getTimestamp("timestamp"));
                    c.setCountTotal(resultSet.getDouble("count_total"));
                    c.setCountRegister1(resultSet.getDouble("count_register1"));
                    c.setCountRegister2(resultSet.getDouble("count_register2"));
                    c.setCountRegister3(resultSet.getDouble("count_register3"));
                    c.setCountRegister4(resultSet.getDouble("count_register4"));
                    c.setPowerP1(resultSet.getDouble("power_p1"));
                    c.setPowerP2(resultSet.getDouble("power_p2"));
                    c.setPowerP3(resultSet.getDouble("power_p3"));
                    c.setWorkP1(resultSet.getDouble("work_p1"));
                    c.setWorkP2(resultSet.getDouble("work_p2"));
                    c.setWorkP3(resultSet.getDouble("work_p3"));
                    c.setFrequency(resultSet.getDouble("frequency"));
                    c.setVoltage(resultSet.getDouble("voltage"));
                    result.AddElement(c);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }
    }

    private class My2vectorsQueryResult extends QueryResult<List<MeterDataEntity>> {

        protected My2vectorsQueryResult(boolean isSuccessful, String errorMessage, QueryStatusCode queryStatusCode) {
            super(isSuccessful, errorMessage, queryStatusCode);
        }

        protected My2vectorsQueryResult() {
            super(true, null, QueryStatusCode.OK);
        }

        private List<MeterDataEntity> data = new ArrayList<>();

        @Override
        public List<MeterDataEntity> getData() {
            return data;
        }

        public void AddElement(MeterDataEntity c) {
            data.add(c);
        }
    }

    private long extractMilisfromTsp(String tspIn) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            tspIn.replace("'", "");
            date = sdf.parse(tspIn);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


}