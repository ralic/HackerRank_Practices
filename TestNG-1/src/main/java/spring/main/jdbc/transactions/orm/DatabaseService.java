package spring.main.jdbc.transactions.orm;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class DatabaseService {

    private JdbcTemplate jdbcTemplate;

    public DatabaseService(DataSource dataSource) {
        System.out.println("Database Service: " + dataSource);
        //Setting data source through constructor
        this.setJdbcTemplate(new JdbcTemplate(dataSource));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
