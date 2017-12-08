package com.hdp.smp.connection;

import com.hdp.smp.Constants;

import com.mchange.v2.c3p0.DataSources;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class MyConnectionProvider implements ConnectionProvider {
    @SuppressWarnings("compatibility:399114649498309170")
    private static final long serialVersionUID = -5971120571035841942L;
    
    public static final Logger log = Logger.getLogger(MyConnectionProvider.class);
    
    static String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
    static String user = "writer011";
    static String password = "test001";
    
    static String dirverName = "com.mysql.jdbc.Driver";
    
    static {
         String dbconnUrl =  Constants.get("hibernate.connection.url");
         if (dbconnUrl != null && dbconnUrl.length() >1) {
                 url = dbconnUrl;
                 log.info("---db url ok:"+url);
             }
         
         String username = Constants.get("hibernate.connection.username");
         if (username != null && username.length() >1 ) {
             user = username;
             log.info("---db user ok:"+user);
             }
         
         String passwd =  Constants.get("hibernate.connection.password");
         if (passwd != null && passwd.length() >1) {
             password = passwd;
             log.info("---db password ok:"+password);
             }
        }
    
    
    static DataSource cpds = getDataSource();
    
   

    static DataSource getDataSource() {
        DataSource pooled = null;
        try {
            Class.forName(dirverName);
            DataSource unpooled = DataSources.unpooledDataSource(url, user, password);
            pooled = DataSources.pooledDataSource(unpooled);

            log.info("--->get the pooled datasource=" + pooled);

            return pooled;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pooled;
    }

    public MyConnectionProvider() {
        super();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
       // log.info("--->Connection "+connection +" closed......");
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class c) {
        return false;
    }

    @Override
    public <T extends Object> T unwrap(Class<T> c) {
        return null;
    }
}
