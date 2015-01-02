import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mysql.MyJDBC;

public class Main {

	static String urlTopic = "http://www.zhihu.com/topic/"; // 知乎话题
	static String urlMovie = urlTopic + "19550429"; // 电影
	static String urlLearn = urlTopic + "19550581"; // 学习
	static String urlFinancial = urlTopic + "19609455"; // 金融
	static String urlProgram = urlTopic + "19554298"; // 编程
	static String urlSoccer = urlTopic + "19559052"; // 足球
	static String urlRecommendations = "http://www.zhihu.com/explore/recommendations";// 编辑推荐

	public static void main(String[] args) {

		// 开5个线程同时抓取
		ExecutorService exec = Executors.newFixedThreadPool(5);
		new MyJDBC().clearDataBase();
		// 构造参数为该线程编号及起始搜索位置
		exec.execute(new Spider("电影", urlMovie));
		exec.execute(new Spider("学习", urlLearn));
		exec.execute(new Spider("金融", urlFinancial));
		exec.execute(new Spider("编程", urlProgram));
		exec.execute(new Spider("足球", urlSoccer));
		exec.shutdown();
	}
}