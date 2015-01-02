import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Data {

	private static String url = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8"; // 数据库地址
	private static String username = "root"; // 数据库用户名
	private static String password = "wulinkai"; // 数据库密码

	static PreparedStatement pStatement = null;
	static Connection con = null;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");//
			con = DriverManager.getConnection(url, username, password);//

		} catch (Exception e) {
		}
		return con;
	}

	public static double getFocusCount(String topic) throws SQLException {
		int count = 0, i = 0;
		double average = 0;
		Connection conn = getConnection();
		String focusCountSQL = "select * from question where topic='" + topic + "'";
		ResultSet rs = null;
		try {
			pStatement = conn.prepareStatement(focusCountSQL);
			rs = pStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (rs.next()) {
			String str = rs.getString("focusCount");
			i++;
			count += Integer.parseInt(str);
			System.out.println(i + ":" + str + "   count:" + count);
		}
		average = (double) count / i;
		System.out.printf("%.2f", average);
		return average;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// getAskStatistics();
			getFocusCount("电影");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
