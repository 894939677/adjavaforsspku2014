package com.datashow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * A frame with sample text components.
 */
public class TextComponentFrame extends JFrame {
	public static final int TEXTAREA_ROWS = 8;
	public static final int TEXTAREA_COLUMNS = 20;

	public TextComponentFrame() {
		final JTextField textField1 = new JTextField();
		final JTextField textField2 = new JTextField();

		JPanel northPanel = new JPanel();
		JButton jb1 = new JButton("查询问题");
		JButton jb2 = new JButton("查询话题");
		northPanel.setLayout(new GridLayout());
		northPanel.add(new JLabel("QuestionID: ", SwingConstants.RIGHT));
		northPanel.add(textField1);
		northPanel.add(jb1);
		northPanel.add(new JLabel("Topic: ", SwingConstants.RIGHT));
		northPanel.add(textField2);
		northPanel.add(jb2);
		add(northPanel, BorderLayout.NORTH);

		final JTextArea textArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLUMNS);
		JScrollPane scrollPane = new JScrollPane(textArea);

		add(scrollPane, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		JButton analysisButton = new JButton("分析数据 ");
		JButton viewButton = new JButton("查看数据 ");
		southPanel.add(analysisButton);
		southPanel.add(viewButton);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					JFrame questionFrame = new QuestionTable();
					questionFrame.setTitle("问题表");
					questionFrame.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		analysisButton.addActionListener(new ActionListener() {
			@SuppressWarnings("null")
			public void actionPerformed(ActionEvent event) {
				try {
					textArea.setText("");
					// StringBuffer stringBuffer = null;
					String[] topics = { "电影", "学习", "金融", "编程", "足球" };
					StringBuffer stringBuffer = new StringBuffer();
					// double[][] allResult = new double[5][2];
					double[] result = null;
					for (String topic : topics) {
						result = DataAnalysis.getResult(topic);
						stringBuffer.append(topic);
						stringBuffer.append("话题    每个问题的平均关注人数:");
						stringBuffer.append(result[0]);
						stringBuffer.append("      每个问题平均回答个数:");
						stringBuffer.append(result[1]);
						stringBuffer.append("\n");
					}
					textArea.append(stringBuffer.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					textArea.setText("");
					String questionID = textField1.getText();
					String result = DataAnalysis.searchQueationByID(questionID);
					textArea.append(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					String topic = textField2.getText();
					JFrame questionFrame = new QuestionTable(topic);
					questionFrame.setTitle("问题表");
					questionFrame.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		add(southPanel, BorderLayout.SOUTH);
		setSize(600, 500);
	}
}
