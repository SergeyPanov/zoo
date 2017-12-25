package cz.vut.fit.zoo.dao;


import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract class contains OracleDataSource for communication with database.
 * Other DAO classes inherit this class thus have access to database through OracleDataSource object.
 * @author xpanov00
 */
public abstract class AbstractDao {


    @Autowired
    private OracleDataSource dataSource;

    OracleDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Delete entity from the table.
     * Query a little bit different for each model class.
     * @param query define the query which delete entity from the particular table.
     */
    public void delete(String query){
        try(Connection connection = dataSource.getConnection()){
            try(Statement statement = connection.createStatement()) {
                statement.executeQuery(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
