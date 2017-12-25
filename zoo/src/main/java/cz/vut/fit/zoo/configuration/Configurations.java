package cz.vut.fit.zoo.configuration;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Configurations class.
 * Bootstrap necessary classes.
 * @author xpanov00
 */
@Configuration
public class Configurations {

    @Value("${oracle.database.user}")
    private String user;

    @Value("${oracle.database.password}")
    private String password;

    @Value("${oracle.database.url}")
    private String url;


    /**
     * Bootstrap OracleDataSource for connecting to DB.
     * @return OracleDataSource
     * @throws SQLException
     */
    @Bean
    public OracleDataSource oracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Bootstrap date-time formatter for format important history points.
     * @return formatter for the history points.
     */
    @Bean
    public SimpleDateFormat tSimpleDateFormat(){
        return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    }

    /**
     * Bootstrap date-time formatter for format live time of some objects.
     * @return formatter for the live time.
     */
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy/MM/dd");
    }


}
