import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

//һ��֪�������һ��������
public class Zhihu {
	public String questionID;// ����ID
	public String questionTitle;// �������
	public String questionDescription;// ��������
	public String zhihuUrl;// ��ҳ����
	public String focusCount;// �������ע����
	public String topic;// ��������
	public JSONObject authorAnswer = new JSONObject(); // ÿ���ش�

	// ���췽����ʼ������
	public Zhihu(String url, String topic) {
		// ��ʼ������
		questionID = "";
		questionTitle = "";
		questionDescription = "";
		zhihuUrl = "";
		focusCount = null;
		this.topic = topic;
		getAllContents(url);
	}

	// ����url
	boolean getRealUrl(String url) {
		// ��http://www.zhihu.com/question/22355264/answer/21102139
		// ת����http://www.zhihu.com/question/22355264
		// ���򲻱�
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			zhihuUrl = "http://www.zhihu.com/question/" + matcher.group(1);
			questionID = matcher.group(1);
		} else {
			zhihuUrl = "http://www.zhihu.com" + url;
			questionID = url.substring(url.lastIndexOf("/") + 1);
		}
		return true;
	}

	public void getAllContents(String url) {
		// �ж�url�Ƿ�Ϸ�
		if (getRealUrl(url)) {
			System.out.println("����ץȡ" + zhihuUrl);
			// ����url��ȡ���ʴ��ϸ��
			String content = Spider.SendGet(zhihuUrl);
			Pattern pattern;
			Matcher matcher;
			// ƥ�����
			pattern = Pattern.compile("zh-question-title.+?<h2.+?>(.+?)</h2>");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				questionTitle = matcher.group(1);
			}
			// ƥ������
			pattern = Pattern.compile("zh-question-detail.+?<div.+?>(.*?)</div>");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				questionDescription = matcher.group(1);
			}

			// ƥ�������ע��
			pattern = Pattern.compile("data-id.+?</button>(.+?)�˹�ע������");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				focusCount = matcher.group(1);
			}

			// ƥ������ID+������+�ش�+�޳���
			String s1 = "href=\"/people/.+?href=\"/people/(.+?)\">(.+?)</a>"; // ƥ������
			String s2 = "data-votecount=\"(.+?)\">"; // ƥ��ûش��޳���
			String s3 = "/answer/content.+?<div.+?>(.*?)</div>"; // ƥ��ش�����
			pattern = Pattern.compile(s1 + ".+?" + s2 + ".+?" + s3);
			matcher = pattern.matcher(content);
			int i = 1;
			boolean isFind = matcher.find();
			JSONObject answerJsonObject = new JSONObject();
			while (isFind) {
				answerJsonObject.put("authorID", matcher.group(1));
				answerJsonObject.put("authorName", matcher.group(2));
				answerJsonObject.put("votes", matcher.group(3));
				answerJsonObject.put("answerContent", matcher.group(4));
				authorAnswer.put("authorAnswer" + i, answerJsonObject);
				isFind = matcher.find();
				i++;
			}
		}
	}

	public String writeString() {
		// ƴ��д�뱾�ص��ַ���
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		String result = "";
		result += "����ʱ�� ��" + dateFormat.format(now) + "\r\n";
		result += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n" + "���⣺" + questionID
				+ "\r\n" + questionTitle + "\r\n\r\n";
		result += "������\r\n" + questionDescription + "\r\n\r\n";
		result += "���ӣ�" + zhihuUrl + "\r\n";
		result += "������ࣺ" + topic + "\r\n";
		result += "��ע������" + focusCount + "\r\n";
		for (int i = 0; i < authorAnswer.size(); i++) {
			result += "_____________________________________\r\n" + "����ID��"
					+ authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorID") + "          "
					+ "�ش�����" + "��" + authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorName")
					+ "\r\n" + "�޳���" + "��" + authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("votes")
					+ "\r\n" + "�ش�����" + "��\r\n"
					+ authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("answerContent") + "\r\n";
		}
		result += "\r\n\r\n\r\n";
		// �����е�html��ǩ����ɸѡ
		result = result.replaceAll("<br>", "\r\n");
		result = result.replaceAll("<.*?>", "");
		return result;
	}
}