import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

//一个知乎类代表一个大问题
public class Zhihu {
	public String questionID;// 问题ID
	public String questionTitle;// 问题标题
	public String questionDescription;// 问题描述
	public String zhihuUrl;// 网页链接
	public String focusCount;// 该问题关注人数
	public String topic;// 所属话题
	public JSONObject authorAnswer = new JSONObject(); // 每个回答

	// 构造方法初始化数据
	public Zhihu(String url, String topic) {
		// 初始化属性
		questionID = "";
		questionTitle = "";
		questionDescription = "";
		zhihuUrl = "";
		focusCount = null;
		this.topic = topic;
		getAllContents(url);
	}

	// 处理url
	boolean getRealUrl(String url) {
		// 将http://www.zhihu.com/question/22355264/answer/21102139
		// 转化成http://www.zhihu.com/question/22355264
		// 否则不变
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
		// 判断url是否合法
		if (getRealUrl(url)) {
			System.out.println("正在抓取" + zhihuUrl);
			// 根据url获取该问答的细节
			String content = Spider.SendGet(zhihuUrl);
			Pattern pattern;
			Matcher matcher;
			// 匹配标题
			pattern = Pattern.compile("zh-question-title.+?<h2.+?>(.+?)</h2>");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				questionTitle = matcher.group(1);
			}
			// 匹配描述
			pattern = Pattern.compile("zh-question-detail.+?<div.+?>(.*?)</div>");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				questionDescription = matcher.group(1);
			}

			// 匹配问题关注数
			pattern = Pattern.compile("data-id.+?</button>(.+?)人关注该问题");
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				focusCount = matcher.group(1);
			}

			// 匹配作者ID+作者名+回答+赞成数
			String s1 = "href=\"/people/.+?href=\"/people/(.+?)\">(.+?)</a>"; // 匹配作者
			String s2 = "data-votecount=\"(.+?)\">"; // 匹配该回答赞成数
			String s3 = "/answer/content.+?<div.+?>(.*?)</div>"; // 匹配回答内容
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
		// 拼接写入本地的字符串
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		String result = "";
		result += "爬虫时间 ：" + dateFormat.format(now) + "\r\n";
		result += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n" + "问题：" + questionID
				+ "\r\n" + questionTitle + "\r\n\r\n";
		result += "描述：\r\n" + questionDescription + "\r\n\r\n";
		result += "链接：" + zhihuUrl + "\r\n";
		result += "话题分类：" + topic + "\r\n";
		result += "关注人数：" + focusCount + "\r\n";
		for (int i = 0; i < authorAnswer.size(); i++) {
			result += "_____________________________________\r\n" + "作者ID："
					+ authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorID") + "          "
					+ "回答作者" + "：" + authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("authorName")
					+ "\r\n" + "赞成数" + "：" + authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("votes")
					+ "\r\n" + "回答内容" + "：\r\n"
					+ authorAnswer.getJSONObject("authorAnswer" + (i + 1)).getString("answerContent") + "\r\n";
		}
		result += "\r\n\r\n\r\n";
		// 将其中的html标签进行筛选
		result = result.replaceAll("<br>", "\r\n");
		result = result.replaceAll("<.*?>", "");
		return result;
	}
}