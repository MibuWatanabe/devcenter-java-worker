import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkerProcess
{
    public static void main(String[] args)
    {
        int count = 0;

        while (true) {
            count++;

            try {
                Thread.sleep(1000 * 30);

                System.out.println("Insert Contact");
                insertContact(count);

                Thread.sleep(1000 * 30);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static Connection getConnection() throws Exception {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    private static void insertContact(int count) throws Exception {
        Connection connection = null;
        Statement statement = null;
        String sql = null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        try {
            connection = getConnection();
            statement = connection.createStatement();
            sql = "INSERT INTO salesforce.DemoObject__c ( ExternalID__c, Comment__c ) VALUES"
                + "( '" + now.format(f) + "', '" + count + "回目の実行です' )"
                + ";";

            System.out.println("insertContact result : " + statement.executeQuery(sql).toString());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
