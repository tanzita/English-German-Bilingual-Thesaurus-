package com.tanziMahbub.lucene;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.awt.Toolkit;
import java.awt.Window.Type;

public class DictionaryMain {

	private JFrame frmDictionary;
	private JTextField queryTextField;
	private JLabel lblLogoLabel;
	private JButton btnSearchButton;
	private JTextField numberTextField;
	private Image imgbtn;
	private Image imgbtn1;
	private JScrollPane scrollPane = new JScrollPane();
	private JLabel lblQuerylabel;
	private JLabel lblNumberLabel;
	private JTable table;
	static JLabel lblInfolabel;

	public static String getIndexDirectory() {

		File f1 = new File("index-directory");
		//File f1 = new File("INDEX");
		String INDEX_DIR = f1.getAbsolutePath();
		
		//System.out.println(INDEX_DIR);

		return INDEX_DIR;
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			int i = Integer.parseInt(str);
			if (i < 1) {
				return false;
			}
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static String[][] performQuery(String queryWord, int numbers, String indexDirectory)
			throws IOException, ParseException {

		// System.out.println("performSearch");
		SearchEngine se = new SearchEngine(indexDirectory);
		TopDocs topDocs = se.performSearch(queryWord, numbers);

		// System.out.println("Results found: " + topDocs.totalHits);

		lblInfolabel.setText("Number of total search items = " + topDocs.totalHits);

		ScoreDoc[] hits = topDocs.scoreDocs;

		if (topDocs.totalHits < numbers)
			numbers = topDocs.totalHits;

		String output[][] = new String[numbers][3];

		for (int i = 0; i < numbers; i++) {
			Document doc = se.getDocument(hits[i].doc);

			output[i][0] = doc.get("eng");
			output[i][1] = doc.get("ger");
			output[i][2] = String.valueOf(hits[i].score);

			// System.out.println(doc.get("eng") + " " + doc.get("ger") + " (" +
			// hits[i].score + ")");
		}
		// System.out.println("performSearch done");

		return output;

	}

	public static void buildIndex(String indexDirectory) {

		try {
			// build a lucene index
			// System.out.println("buildIndexes");

			Indexer indexer = new Indexer(indexDirectory);
			indexer.buildIndexes();
			// System.out.println("buildIndexes done");

		} catch (Exception e) {
			System.out.println("Exception caught.\n");
		}

	}

	/**
	 * Launch the application.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryMain window = new DictionaryMain();
					window.frmDictionary.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		String indexPath = getIndexDirectory();
		buildIndex(indexPath);
	}

	/**
	 * Create the application.
	 */
	public DictionaryMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDictionary = new JFrame();
		frmDictionary.setType(Type.POPUP);
		frmDictionary.setTitle("English-German Bilingual Thesaurus");
		frmDictionary.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(DictionaryMain.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		frmDictionary.setResizable(false);
		frmDictionary.setBounds(100, 100, 805, 616);
		frmDictionary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDictionary.getContentPane().setLayout(null);

		/****** Search Button ***********/
		btnSearchButton = new JButton("Search");

		imgbtn = new ImageIcon(this.getClass().getResource("search.png")).getImage();

		btnSearchButton.setIcon(new ImageIcon(imgbtn));

		/****** setting out put on click *****/

		btnSearchButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// code here
				String queryToken = null;
				String searchCountChoice = null;
				String indexPath = getIndexDirectory();

				queryToken = queryTextField.getText();

				// System.out.println("result:" + queryToken + ":");

				searchCountChoice = numberTextField.getText();
				if (queryToken.equals("")) {
					lblInfolabel.setText("Query Field is Empty");
				} else if (isInteger(searchCountChoice) == false) {
					lblInfolabel.setText("Invalid Number");
				} else {
					try {
						String[][] rowData = performQuery(queryToken, Integer.parseInt(searchCountChoice), indexPath);

						String columnNames[] = { "English", "German", "Hit Count" };

						table = new JTable(rowData, columnNames);
						table.setForeground(new Color(0, 0, 204));
						table.setBackground(Color.LIGHT_GRAY);
						table.setColumnSelectionAllowed(false);
						table.setCellSelectionEnabled(false);

						table.setDefaultEditor(Object.class, null);

						//scrollPane = new JScrollPane(table);
						scrollPane.setFocusable(true);
						scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
						scrollPane.setForeground(Color.RED);
						scrollPane.setBackground(Color.CYAN);
						scrollPane.setBounds(28, 199, 745, 351);
						scrollPane.getViewport().add( table );

						frmDictionary.getContentPane().add(scrollPane);

					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnSearchButton.setBounds(677, 105, 96, 23);
		frmDictionary.getContentPane().add(btnSearchButton);

		lblInfolabel = new JLabel("");
		lblInfolabel.setBounds(28, 158, 394, 23);
		lblInfolabel.setForeground(Color.RED);
		frmDictionary.getContentPane().add(lblInfolabel);

		queryTextField = new JTextField();
		queryTextField.setBounds(282, 87, 349, 23);
		frmDictionary.getContentPane().add(queryTextField);
		queryTextField.setColumns(10);

		queryTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				queryTextField.setText("");
				lblInfolabel.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});

		lblLogoLabel = new JLabel("");
		imgbtn1 = new ImageIcon(this.getClass().getResource("dict.png")).getImage();
		lblLogoLabel.setIcon(new ImageIcon(imgbtn1));
		lblLogoLabel.setBounds(358, 13, 64, 64);
		frmDictionary.getContentPane().add(lblLogoLabel);

		numberTextField = new JTextField();
		numberTextField.setBounds(515, 127, 116, 22);
		frmDictionary.getContentPane().add(numberTextField);
		numberTextField.setColumns(10);

		lblQuerylabel = new JLabel(" Enter Your Query (English or German)");
		lblQuerylabel.setBounds(28, 87, 242, 23);
		frmDictionary.getContentPane().add(lblQuerylabel);

		lblNumberLabel = new JLabel(" Enter how many result that you want (Please enter a valid number)");
		lblNumberLabel.setBounds(28, 127, 440, 23);
		frmDictionary.getContentPane().add(lblNumberLabel);
		
		//scrollPane = new JScrollPane();
		/*scrollPane.setFocusable(true);
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPane.setForeground(Color.RED);
		scrollPane.setBackground(Color.CYAN);
		scrollPane.setBounds(28, 199, 745, 351);
		scrollPane.getViewport().add( table );
		frmDictionary.getContentPane().add(scrollPane);*/

	}
}
