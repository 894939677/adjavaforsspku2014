import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mysql.MyJDBC;

public class Main {

	static String urlTopic = "http://www.zhihu.com/topic/"; // ֪������
	static String urlMovie = urlTopic + "19550429"; // ��Ӱ
	static String urlLearn = urlTopic + "19550581"; // ѧϰ
	static String urlFinancial = urlTopic + "19609455"; // ����
	static String urlProgram = urlTopic + "19554298"; // ���
	static String urlSoccer = urlTopic + "19559052"; // ����
	static String urlRecommendations = "http://www.zhihu.com/explore/recommendations";// �༭�Ƽ�

	public static void main(String[] args) {

		// ��5���߳�ͬʱץȡ
		ExecutorService exec = Executors.newFixedThreadPool(5);
		new MyJDBC().clearDataBase();
		// �������Ϊ���̱߳�ż���ʼ����λ��
		exec.execute(new Spider("��Ӱ", urlMovie));
		exec.execute(new Spider("ѧϰ", urlLearn));
		exec.execute(new Spider("����", urlFinancial));
		exec.execute(new Spider("���", urlProgram));
		exec.execute(new Spider("����", urlSoccer));
		exec.shutdown();
	}
}