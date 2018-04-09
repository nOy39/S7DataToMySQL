package dbconnector;

import helpers.Config;
import helpers.Const;
import helpers.Refrigerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseHandler extends Config {

    Connection dbConfigConnection;

    public Connection getDbConnection() throws SQLException, ClassNotFoundException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName +
                "?autoReconnect=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                "&useLegacyDatetimeCode=false&serverTimezone=UTC";

        Class.forName("com.mysql.jdbc.Driver");

        dbConfigConnection = DriverManager.getConnection(connectionString,dbUser,dbPass);

        return dbConfigConnection;
    }

    public void setRefToBase(Refrigerator refCabin) {

        String insert = "INSERT INTO " + Const.DB_TABLE + "(" + Const.OUTPUT_CODE + "," +
                Const.OUTPUT_POL_EX + "," + Const.OUTPUT_ISO_EX + "," +
                Const.OUTPUT_DATE + "," + Const.OUTPUT_TIME + ") VALUES (?, ?, ?, ?, ?)";


        PreparedStatement pStatment = null;

        try {
            pStatment = getDbConnection().prepareStatement(insert);
            pStatment.setInt(1, refCabin.getCode());
            pStatment.setInt(2, refCabin.getPol_expand());
            pStatment.setInt(3, refCabin.getIso_expand());
            pStatment.setString(4, refCabin.getDate());
            pStatment.setString(5, refCabin.getTime());

            pStatment.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
