import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.json.JSONObject;

public class DataAnalysis {

	private static String url = "jdbc:mysql://localhost:3306/zhihu?useUnicode=true&characterEncoding=utf8"; // ���ݿ��ַ
	private static String username = "root"; // ���ݿ��û���
	private static String password = ""; // ���ݿ�����

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
	

	
	public DataAnalysis() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * ���룺questionID
	 * �����1���ַ�����questionTitle��topic��  
	 * 	   2��JSON����answers���������ܶ�answer����0��ʼ������
	 * 			answer����������authorName��������voteCount������answerCount
	 * */
	private static void getQueationByID(String questionID){
		String questionTitle="",topic="";
		
		Connection conn=getConnection();
		//��ѯquestion��
		String sql1="select * from question where questionID='" + questionID + "'";
		System.out.println(sql1);
		try {
			pStatement = conn.prepareStatement(sql1);
			ResultSet rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				questionTitle = rs1.getString("questionTitle");
				topic = rs1.getString("topic");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//��ѯ����һ��author��
		JSONObject answers=new JSONObject();
		int i=0;
		String sql2="select * from author where questionID='" + questionID + "'";
		try {
			pStatement = conn.prepareStatement(sql2);
			ResultSet rs2 = pStatement.executeQuery();
			while (rs2.next()) {
				JSONObject answer=new JSONObject();
				answer.put("authorName", rs2.getString("authorName"));
				answer.put("voteCount", rs2.getString("voteCount"));
				answer.put("answerCount", rs2.getString("answerCount"));
				answers.put(""+i, answer);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject question=new JSONObject();
		question.put("questionTitle", questionTitle);
		question.put("topic", topic);
		question.put("answers", answers);
		System.out.println(question.toString());
	}
	/*
	 * ���룺topic
	 * ����� JSON����questions���������ܶ�question����0��ʼ������
	 * 			question��questionTitle,zhihuUrl
	 * */
	public static void getQuestionsBytopic(String topic) {
		Connection conn=getConnection();
		//����Ӧ��topic����ȡquestionID�б�
		String sql1="SELECT * FROM `"+ topic+ "` ";
		int i=0;
		JSONObject questionIDs=new JSONObject();
		try {
			pStatement = conn.prepareStatement(sql1);
			ResultSet rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				questionIDs.put(""+i, rs1.getString("questionID"));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		i=0;
		for(int j=0;j<questionIDs.size();j++){
			String questionID=questionIDs.getString(""+j);
			
			//��question���и���questionIDȡ����Ӧ��������⣬����
			
			JSONObject questions=new JSONObject();
			String sql2="SELECT * FROM `question` where questionID='" + questionID + "'";
			System.err.println(sql2);
			try {
				pStatement = conn.prepareStatement(sql2);
				ResultSet rs2 = pStatement.executeQuery();
				while (rs2.next()) {
					JSONObject question=new JSONObject();
					question.put("questionTitle", rs2.getString("questionTitle"));
					question.put("zhihuUrl", rs2.getString("zhihuUrl"));
					questions.put(""+i, question);
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(questions.toString());
			
		}

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
//		try {
//			// getAskStatistics();
//			getFocusCount("��Ӱ");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//getQuestionsBytopic("learn");
		getQueationByID("24326030");
	}
}
