import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
    @Test
    void connectionTest() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307", "root", "1234");
        Assertions.assertNotNull(conn);
    }
}
