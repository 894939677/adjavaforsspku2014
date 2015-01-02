import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spider implements Runnable {

	String topic;
	String urlString;
	String content;
	ArrayList<Zhihu> myZhihu = new ArrayList<Zhihu>();

	public Spider(String topic, String url) {
		urlString = url;
		this.topic = topic;
	}

	public void run() {
		try {

			content = Spider.SendGet(urlString);
			// ��ȡ��������
			myZhihu = Spider.GetTopics(content, topic);
			// ��ӡ��� д�뱾��
			LoadData loadDate = new LoadData(myZhihu);
			loadDate.loadQuestion();
			loadDate.LoadTopic();
			loadDate.loadAuthors();
			for (Zhihu zhihu : myZhihu) {
				FileReaderWriter.writeIntoFile(zhihu.writeString(), "D:/֪��_" + topic + ".doc", true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	static String SendGet(String url) {
		// ����һ���ַ��������洢��ҳ����
		String result = "";
		// ����һ�������ַ�������
		BufferedReader in = null;
		try {
			// ��stringת��url����
			URL realUrl = new URL(url);
			// ��ʼ��һ�����ӵ��Ǹ�url������
			URLConnection connection = realUrl.openConnection();
			// ��ʼʵ�ʵ�����
			connection.connect();
			// ��ʼ�� BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			// ������ʱ�洢ץȡ����ÿһ�е�����
			String line;
			while ((line = in.readLine()) != null) {
				// ����ץȡ����ÿһ�в�����洢��result����
				result += line;
			}
		} catch (Exception e) {
			System.out.println("����GET��������쳣��" + e);
			e.printStackTrace();
		}
		// ʹ��finally���ر�������
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.out.println("��ȡ���ݳɹ�!");
		}
		return result;
	}

	// ��ȡ���е���ػ����֪������
	static ArrayList<Zhihu> GetTopics(String content, String topic) {
		// Ԥ����һ��ArrayList���洢���
		ArrayList<Zhihu> results = new ArrayList<Zhihu>();
		// ����ƥ��url��Ҳ�������������
		Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?</h2>");
		Matcher matcher = pattern.matcher(content);
		// �Ƿ����ƥ��ɹ��Ķ���
		Boolean isFind = matcher.find();
		while (isFind) {
			// ����һ��֪���������洢ץȡ������Ϣ
			Zhihu zhihuTemp = new Zhihu(matcher.group(1), topic);
			// ��ӳɹ�ƥ��Ľ��
			results.add(zhihuTemp);
			// ����������һ��ƥ�����
			isFind = matcher.find();
		}
		return results;
	}

}