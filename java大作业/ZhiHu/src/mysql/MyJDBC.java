package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MyJDBC {

	private static String url = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8"; // ���ݿ��ַ
	private static String username = "root"; // ���ݿ��û���
	private static String password = "wulinkai"; // ���ݿ�����

	private Connection conn = null;
	private Statement st = null;
	private PreparedStatement pStatement = null;

	public MyJDBC() {
		// TODO Auto-generated constructor stub
	}

	// ���������
	public synchronized void SaveQuestions(String questionID, String questionTitle, String zhihuUrl, String focusCount,
			String topic, String answerCount) {
		conn = getConnection();// ��ȡ����
		String sql;
		try {
			sql = "INSERT INTO `question` (`questionID`, `questionTitle`, `zhihuUrl`, `focusCount`, `topic`, `answerCount`) VALUES (?,?,?,?,?,?)";
			pStatement = conn.prepareStatement(sql);// ����prepareStatement����
			pStatement.setString(1, questionID);
			pStatement.setString(2, questionTitle);
			pStatement.setString(3, zhihuUrl);
			pStatement.setString(4, focusCount);
			pStatement.setString(5, topic);
			pStatement.setString(6, answerCount);
			pStatement.executeUpdate();
			conn.close();
		} catch (SQLException e) {
		}
	}

	// �����������������Ϣ����¼���������ID
	public synchronized boolean SaveTopic(String Topic, String questionID, String answercount) {
		conn = getConnection();// ��ȡ����
		String topic = "";
		String sql;
		try {
			switch (Topic) {
			case "��Ӱ":
				topic = "movie";
				break;
			case "ѧϰ":
				topic = "learn";
				break;
			case "����":
				topic = "soccer";
				break;
			case "����":
				topic = "financial";
				break;
			case "���":
				topic = "program";
				break;
			default:
				break;
			}
			sql = "INSERT INTO `" + topic + "` (`questionID`,`answerCount`) VALUES(?,?);";
			pStatement = conn.prepareStatement(sql);// ����prepareStatement����
			pStatement.setString(1, questionID);
			pStatement.setString(2, answercount);
			pStatement.executeUpdate();
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	// �������ݿ��е����߱���
	public synchronized boolean SaveAuthors(String authorID, String authorname, String votecount, String answercount,
			String questionID) {
		conn = getConnection();// ��ȡ����
		String sql;
		try {
			sql = "INSERT INTO `author` (`authorID`, `authorName`, `voteCount`, `answerCount`, `questionID`) VALUES ('"
					+ authorID + "', '" + authorname + "', '" + votecount + "', '" + answercount + "', '" + questionID
					+ "');";
			st = (Statement) conn.createStatement();// ����Statement����
			st.executeUpdate(sql);
			conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");//
			con = DriverManager.getConnection(url, username, password);//

		} catch (Exception e) {
		}
		return con;
	}

	public void clearDataBase() {
		conn = getConnection();// ��ȡ����
		try {
			st = (Statement) conn.createStatement();// ����Statement����
			st.execute("TRUNCATE TABLE author");
			st.execute("TRUNCATE TABLE financial");
			st.execute("TRUNCATE TABLE movie");
			st.execute("TRUNCATE TABLE soccer");
			st.execute("TRUNCATE TABLE program");
			st.execute("TRUNCATE TABLE learn");
			st.execute("TRUNCATE TABLE question");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
