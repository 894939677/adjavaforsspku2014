package com.datashow;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A frame with sample text components.
 */
public class QuestionTable extends JFrame {
	JTable JTableView;
	JScrollPane scrollPane;

	public QuestionTable() {
		JTableView = DataAnalysis.getQuestion();
		scrollPane = new JScrollPane(JTableView);
		add(scrollPane);
		setSize(900, 700);
	}

	public QuestionTable(String topic) {
		JTableView = DataAnalysis.searchTopic(topic);
		scrollPane = new JScrollPane(JTableView);
		add(scrollPane);
		setSize(900, 700);
	}

}
