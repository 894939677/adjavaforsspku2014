import java.util.ArrayList;

import mysql.MyJDBC;

public class LoadData {
	public static MyJDBC myJDBC = new MyJDBC();
	ArrayList<Zhihu> myZhihu = new ArrayList<Zhihu>();

	public LoadData(ArrayList<Zhihu> myZhihu) {
		this.myZhihu = myZhihu;
	}

	// 将数据载入数据库的话题和作者表中
	public synchronized void loadAuthors() {
		String answerCount = "";// 答者回答字数
		String authorID = ""; // 答者ID
		String authorName = "";// 答者名
		String voteCount = "";// 答者得票
		String answerContent = "";// 回答内容

		for (Zhihu zh : myZhihu) {
			for (int i = 0; i < zh.authorAnswer.size(); i++) {
				authorID = zh.authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorID");
				authorName = zh.authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorName");
				voteCount = zh.authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("votes");
				answerContent = zh.authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("answerContent");
				answerContent = answerContent.replaceAll("<br>", "\r\n");
				answerContent = answerContent.replaceAll("<.*?>", "");
				answerCount = getCounts(answerContent);
				myJDBC.SaveAuthors(authorID, authorName, voteCount, answerCount, zh.questionID);
			}
		}
	}

	// 载入数据库中的问题表
	public synchronized void loadQuestion() {
		int answerCount = 0;
		for (Zhihu zh : myZhihu) {
			answerCount = zh.authorAnswer.size();
			myJDBC.SaveQuestions(zh.questionID, zh.questionTitle, zh.zhihuUrl, zh.focusCount, zh.topic,
					Integer.toString(answerCount));
		}
	}

	public synchronized void LoadTopic() {
		for (Zhihu zhi : myZhihu) {
			String answerCount = Integer.toString(zhi.authorAnswer.size());
			myJDBC.SaveTopic(zhi.topic, zhi.questionID, answerCount);
		}

	}

	public String getCounts(String answerContent) {
		int size = answerContent.length();
		return Integer.toString(size);
	}
}
