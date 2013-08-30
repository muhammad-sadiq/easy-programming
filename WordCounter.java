/***************************************************
 * @author easy programming                        *
 * A simple Word Frequency Example using HashMap   *
 * Aug 30 2013                                     *
 *                                                 *
 * Although wordCounter program can be created     *
 * with Hadoop or simply by using pig language     *
 * But here i'll show you that how can we create   *
 * it with Java HashMap which shows the frequency  *
 * of each word in a text.                         *
 ***************************************************/
//package com.projects.easyprogramming;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class WordCounter extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -603841049542334604L;

	/**
	 * @param args
	 */
	protected Map<String, Integer> wordList;
	protected JTextArea text, result;
	protected JButton countButton, clearButton;
	
	public WordCounter(String appTitle){
		super(appTitle);
		Container c = this.getContentPane();
		c.setLayout(new FlowLayout());
		//wordList is a HashMap which creates word frequency table.
		wordList = new HashMap<String, Integer>();
		
		//Creating a TextArea to hold the input text.
		text = new JTextArea(10, 50);
		
		//Adding Scroll panes to TextArea.
		JScrollPane textArea = new JScrollPane ( text );
		textArea.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		countButton = new JButton("Count");
		clearButton = new JButton("Clear");
		
		countButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		JLabel label = new JLabel("Enter Text");
		c.add(label);
		c.add(textArea);
		c.add(countButton);
		c.add(clearButton);
		
		result = new JTextArea(10, 50);
		result.setEditable(false);
		JScrollPane resultArea = new JScrollPane ( result );
		resultArea.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		
		c.add(resultArea);
		
		//Basic JFrame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 450);
		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new WordCounter("Word Counter");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getSource() == countButton)
			countWords();
		else
			clearBoth();
			

	}
	/**
	 * @param 
	 * @return void
	 * @Operation removes the text from both textarea. 
	 */
	private void clearBoth() {
		// TODO Auto-generated method stub
		text.setText("");
		result.setText("");
	}
	/**
	 * @param
	 * @return
	 * @operation creates a word frequency table by using HashMap
	 */
	private void countWords() {
		// TODO Auto-generated method stub
		//Patten.compile() is used to filter the input text and to use the following regex as a delimiter 
		String[] msg = Pattern.compile("[-/\\,+.*\\s&]+").split(text.getText(), 0);
		
		for(String word : msg){
			
			if( wordList.get(word) != null )
				wordList.put(word, wordList.get(word)+1 );
			else
				wordList.put(word, 1);
		}
		result.setText(getResult());
	}

	/**
	 * @return String
	 * @Operation Iterates over HashMap and returns the result.
	 */
	private String getResult() {
		// TODO Auto-generated method stub
		StringBuilder wordFrequency = new StringBuilder("word\tfrequency\n");
		wordFrequency.append("------------------------------------\n");
		
		Iterator<Entry<String, Integer>> it = wordList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
	    	
	        wordFrequency.append(pairs.getKey() + "\t" + pairs.getValue()+"\n");
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return wordFrequency.toString();
	}

}
