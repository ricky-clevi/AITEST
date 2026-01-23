import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixBLCPageTable {
    public static void main(String[] args) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/broadleaf", "SA", "");
            Statement stmt = conn.createStatement();
            
            // Add missing columns to BLC_PAGE table
            String[] columns = {
                "DATE_CREATED TIMESTAMP",
                "CREATED_BY BIGINT",
                "DATE_UPDATED TIMESTAMP",
                "UPDATED_BY BIGINT"
            };
            
            for (String column : columns) {
                try {
                    String sql = "ALTER TABLE PUBLIC.BLC_PAGE ADD COLUMN " + column;
                    stmt.execute(sql);
                    System.out.println("Added column: " + column);
                } catch (Exception e) {
                    System.out.println("Column may already exist: " + column);
                }
            }
            
            stmt.close();
            conn.close();
            System.out.println("BLC_PAGE table updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}