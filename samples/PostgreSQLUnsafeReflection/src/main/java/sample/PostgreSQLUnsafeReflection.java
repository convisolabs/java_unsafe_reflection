// CVE-2022-21724

import java.sql.DriverManager;
import java.sql.SQLException;

class PostgreSQLUnsafeReflection {
    public static void main(String[] args) throws SQLException {
        System.out.println("Supplied argument: " + args[0]);
        DriverManager.registerDriver(new org.postgresql.Driver());
        DriverManager.getConnection(args[0]);
    }
}
