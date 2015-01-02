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
		JButton jb1 = new JButton("��ѯ����");
		JButton jb2 = new JButton("��ѯ����");
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
		JButton analysisButton = new JButton("�������� ");
		JButton viewButton = new JButton("�鿴���� ");
		southPanel.add(analysisButton);
		southPanel.add(viewButton);
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					JFrame questionFrame = new QuestionTable();
					questionFrame.setTitle("�����");
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
					String[] topics = { "��Ӱ", "ѧϰ", "����", "���", "����" };
					StringBuffer stringBuffer = new StringBuffer();
					// double[][] allResult = new double[5][2];
					double[] result = null;
					for (String topic : topics) {
						result = DataAnalysis.getResult(topic);
						stringBuffer.append(topic);
						stringBuffer.append("����    ÿ�������ƽ����ע����:");
						stringBuffer.append(result[0]);
						stringBuffer.append("      ÿ������ƽ���ش����:");
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
					questionFrame.setTitle("�����");
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
