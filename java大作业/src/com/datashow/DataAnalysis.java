package com.datashow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataAnalysis {

	private static String url = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8"; // 数据库地址
	private static String username = "root"; // 数据库用户名
	private static String password = "wulinkai"; // 数据库密码

	static PreparedStatement pStatement = null;
	static Connection con = null;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
		}
		return con;
	}

	public DataAnalysis() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * 输入：questionID 输出：1、字符串：questionTitle、topic、url 、关注人数
	 * 2、作者名字authorName，点赞数voteCount，字数answerCount
	 */
	public static String searchQueationByID(String questionID) {
		Connection conn = getConnection();
		// 查询question表
		String sql = "select * from question where questionID = '" + questionID + "'";
		String sql2 = "select * from author where questionID= '" + questionID + "'";
		ResultSet rs = null;
		StringBuffer sBuffer = new StringBuffer();
		String questionTitle = "";
		String url = "";
		String focusCount = "";
		String topic = "";
		String authorID = "";
		String authorName = "";
		String voteCount = "";
		String answerCount = "";

		try {
			pStatement = conn.prepareStatement(sql);
			rs = pStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (rs.next()) {
				focusCount = rs.getString("focusCount");
				questionTitle = rs.getString("questionTitle");
				url = rs.getString("zhihuUrl");
				topic = rs.getString("topic");
			}
			sBuffer.append("ID:" + questionID + "   \n标题：" + questionTitle + "\n");
			sBuffer.append("主题:" + topic + "\t问题关注人数：" + focusCount + "\n网址：" + url + "\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			pStatement = conn.prepareStatement(sql2);
			rs = pStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				authorID = rs.getString("authorID");
				authorName = rs.getString("authorName");
				voteCount = rs.getString("voteCount");
				answerCount = rs.getString("answerCount");
				sBuffer.append("用户ID:" + authorID + "              " + "用户名:" + authorName + "             " + "赞同数:"
						+ voteCount + "         " + "回答字数" + answerCount + '\n');
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sBuffer.toString();
	}

	public static JTable searchTopic(String topic) {
		Connection conn = getConnection();

		// 查询question表
		String sql = "select * from question where topic = '" + topic + "'";
		Vector<String> colum = new Vector<>();
		Vector<Vector<String>> rows = new Vector<>();
		JTable table = null;
		ResultSet rs = null;
		try {
			pStatement = conn.prepareStatement(sql);
			rs = pStatement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); ++i)
				colum.add(rsmd.getColumnName(i));
			while (rs.next()) {
				Vector<String> currow = new Vector<String>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					currow.add(rs.getString(i));
				}
				rows.add(currow);
			}
			DefaultTableModel tableModel = new DefaultTableModel(rows, colum);
			table = new JTable(tableModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}

	public static JTable getQuestion() {
		Connection conn = getConnection();

		// 查询question表
		String sql = "select * from question ";
		Vector<String> colum = new Vector<>();
		Vector<Vector<String>> rows = new Vector<>();
		JTable table = null;
		ResultSet rs = null;
		try {
			pStatement = conn.prepareStatement(sql);
			rs = pStatement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); ++i)
				colum.add(rsmd.getColumnName(i));
			while (rs.next()) {
				Vector<String> currow = new Vector<String>();
				for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
					currow.add(rs.getString(i));
				}
				rows.add(currow);
			}
			DefaultTableModel tableModel = new DefaultTableModel(rows, colum);
			table = new JTable(tableModel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}

	public static double[] getResult(String topic) throws SQLException {
		double[] result = new double[2];
		int focusCount = 0, answerCount = 0, i = 0;
		double averageFocus = 0, averageAnswers = 0;
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
			String strFocusCount = rs.getString("focusCount");
			String strAnswerCount = rs.getString("answerCount");
			i++;
			focusCount += Integer.parseInt(strFocusCount);
			answerCount += Integer.parseInt(strAnswerCount);
		}
		averageFocus = (double) focusCount / i;
		averageAnswers = (double) answerCount / i;
		averageFocus = Double.parseDouble(String.format("%.2f", averageFocus));
		averageAnswers = Double.parseDouble(String.format("%.2f", averageAnswers));
		result[0] = averageFocus;
		result[1] = averageAnswers;
		return result;

	}

	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	//
	// try {
	// getResult("足球");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
}
