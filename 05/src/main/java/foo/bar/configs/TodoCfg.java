package foo.bar.configs;

import io.vertx.core.Vertx;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;

import java.io.ByteArrayInputStream;
import java.util.Properties;

public class TodoCfg {

    private final Vertx vertx;

    private int serverPort = 8080;
    private String dbUrl = "jdbc:h2:./todos.h2;DB_CLOSE_DELAY=-1";
    private String dbUser = "sa";
    private String dbPass = "";
    private int dbPoolSize = 5;

    public TodoCfg (Vertx vertx) throws Exception {
        this.vertx = vertx;
        Properties props = new Properties();
        props.load(TodoCfg.class.getClassLoader()
            .getResourceAsStream("application.properties"));
        serverPort = Integer.parseInt(props.getProperty("server.port"));
        dbUrl = props.getProperty("db.url");
        dbUser = props.getProperty("db.user");
        dbPass = props.getProperty("db.pass");
        dbPoolSize = Integer.parseInt(props.getProperty("db.pool.size"));
    }

    public JDBCPool configurePool() {
        JDBCConnectOptions connectOptions = new JDBCConnectOptions()
            .setJdbcUrl(dbUrl)
            .setUser(dbUser)
            .setPassword(dbPass);
        PoolOptions poolOptions = new PoolOptions().setMaxSize(dbPoolSize);
        return JDBCPool.pool(vertx, connectOptions, poolOptions);
    }

    public int getServerPort() {
        return serverPort;
    }
}
