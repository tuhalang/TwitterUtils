package com.tuhalang.twitterutils.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final Logger LOGGER = Logger.getLogger(DBUtils.class);

    public static boolean isExist(String id){
        String sql = "select * from tweet t where t.id=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            statement = conn.prepareStatement(sql);
            statement.setQueryTimeout(2);
            statement.setString(1, id);
            rs = statement.executeQuery();
            if(rs.next()){
                LOGGER.debug("Key" + id + " is exists !");
                return true;
            }else{
                LOGGER.debug("Key" + id + " is not exists !");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
