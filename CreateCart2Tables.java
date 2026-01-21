import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;

public class CreateCart2Tables {
    public static void main(String[] args) {
        try {
            // HSQLDB 연결
            Connection conn = DriverManager.getConnection(
                "jdbc:hsqldb:hsql://localhost:9001/broadleaf",
                "sa",
                ""
            );
            
            Statement stmt = conn.createStatement();
            
            // SQL 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader("LegacyDemoSite/core/src/main/resources/sql/create_cart2_tables.sql"));
            StringBuilder sql = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                // 주석과 빈 줄 무시
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    sql.append(line).append("\n");
                }
            }
            reader.close();
            
            // SQL 실행 (각 문장 분리)
            String[] sqlStatements = sql.toString().split(";");
            for (String sqlStatement : sqlStatements) {
                if (!sqlStatement.trim().isEmpty()) {
                    try {
                        stmt.execute(sqlStatement);
                        System.out.println("실행 성공: " + sqlStatement.trim().substring(0, Math.min(50, sqlStatement.trim().length())));
                    } catch (Exception e) {
                        System.out.println("실행 실패: " + e.getMessage());
                    }
                }
            }
            
            stmt.close();
            conn.close();
            
            System.out.println("\nCart2 테이블 생성 완료!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}