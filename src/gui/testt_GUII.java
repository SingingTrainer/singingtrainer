package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import melodic.MelodicMain;
import rhythmic.RhythmicMain;

import org.json.simple.*;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class testt_GUII extends JFrame {
	MelodicMain mel;
	RhythmicMain rit;
	private JFrame frame;
	private JButton but1 = new JButton("Play");
	private JButton but2 = new JButton("Start Rec");
	private JButton but3 = new JButton("Finish Rec");
	private JButton but4 = new JButton("Analyze");
	private static JTextField textField;

	JSONParser parser; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testt_GUII window = new testt_GUII();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws InvalidMidiDataException 
	 */
	public testt_GUII() throws InvalidMidiDataException, IOException {
		initialize();
		//mainWindow1();
		//mainWindow2();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblPleaseEnterUsername = new JLabel("Please enter username");
		lblPleaseEnterUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseEnterUsername.setBounds(0, 51, 388, 16);
		frame.getContentPane().add(lblPleaseEnterUsername);

		textField = new JTextField();
		textField.setBounds(124, 79, 130, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Please select one of trainer");
		lblNewLabel.setBounds(124, 117, 181, 16);
		frame.getContentPane().add(lblNewLabel);

		JRadioButton rdbtnMelodicTrainer = new JRadioButton("Melodic Trainer");
		rdbtnMelodicTrainer.setBounds(124, 145, 141, 23);
		frame.getContentPane().add(rdbtnMelodicTrainer);

		JRadioButton rdbtnRhytmicTrainer = new JRadioButton("Rhytmic Trainer");
		rdbtnRhytmicTrainer.setBounds(124, 180, 141, 23);
		frame.getContentPane().add(rdbtnRhytmicTrainer);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnMelodicTrainer);
		buttonGroup.add(rdbtnRhytmicTrainer);

		JButton btnLogIn = new JButton("Log In");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Please select one of the trainer type!");
				if(rdbtnMelodicTrainer.isSelected()==false && rdbtnRhytmicTrainer.isSelected()==false){
					JOptionPane.showMessageDialog(null, "Please select one of the trainer type!");
				}else if(rdbtnMelodicTrainer.isSelected()==true && rdbtnRhytmicTrainer.isSelected()==false){
					frame.setVisible(false);
					try {
						readMelodic();
						mainWindow1();

					} catch (InvalidMidiDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(rdbtnMelodicTrainer.isSelected()==false && rdbtnRhytmicTrainer.isSelected()==true){
					frame.setVisible(false);
					try {
						readRhytmic();
						mainWindow2();
					} catch (InvalidMidiDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				if(textField.getText().compareTo("taylan")==0){
					frame.setVisible(false);
					//mainWindow2();



				}
			}
		});
		btnLogIn.setBounds(134, 215, 117, 29);
		frame.getContentPane().add(btnLogIn);
	}

	private void mainWindow1() throws InvalidMidiDataException, IOException {
		
		this.setTitle("Singing Trainer");

		frame = new JFrame();
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);


		JLabel exerInfo = new JLabel("Exercise Info");
		exerInfo.setFont(new Font("SansSerif",Font.BOLD, 18));
		exerInfo.setBounds(406, 6, 140, 16);
		frame.getContentPane().add(exerInfo);

		ButtonGroup buttonGroup = new ButtonGroup();

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 115, 738, 12);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 415, 738, 16);
		frame.getContentPane().add(separator_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(382, 6, 12, 97);
		frame.getContentPane().add(separator_3);

		JButton playButton = new JButton("Play");
		playButton.setBounds(6, 16, 62, 29);
		frame.getContentPane().add(playButton);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mel.getExGen().playEx();

			}
		});
		JButton btnStartRec = new JButton("Start Rec");
		JButton btnRecord = new JButton("Finish Rec");
		btnRecord.setBounds(185, 16, 93, 29);
		frame.getContentPane().add(btnRecord);
		btnRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnStartRec.setEnabled(true);
				btnRecord.setEnabled(false);

				mel.getTargetDataLine().stop();
				mel.getTargetDataLine().close();

			}
		});

		btnStartRec.setBounds(80, 16, 93, 29);
		frame.getContentPane().add(btnStartRec);
		btnStartRec.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnStartRec.setEnabled(false);
				btnRecord.setEnabled(true);
				mel.captureAudio();
			}
		});

		JButton prev = new JButton("< Previous");
		prev.setBounds(406, 90, 117, 29);
		prev.setEnabled(true);
		frame.getContentPane().add(prev);

		JButton next = new JButton("Next >");
		next.setBounds(535, 90, 117, 29);
		next.setEnabled(mel.nextFlag);
		frame.getContentPane().add(next);

		JLabel label = new JLabel(String.valueOf(mel.getExNo()));
		JLabel lblA = new JLabel(String.valueOf(mel.getLevel()));

		prev.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						int exNum = mel.getExGen().getExNum();
						mel.getExGen().setExNum(exNum-1);
						mel.getExGen().createEx();
						label.setText(""+(exNum-1));
					}//end actionPerformed
				}//end ActionListener
				);
		next.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						int exNum = mel.getExGen().getExNum();
						mel.getExGen().setExNum(exNum+1);
						mel.getExGen().createEx();
						label.setText(""+(exNum+1));
					}//end actionPerformed
				}//end ActionListener
				);

		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setBounds(290, 16, 80, 29);
		frame.getContentPane().add(btnAnalyze);
		btnAnalyze.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						mel.analyze();
						next.setEnabled(mel.isNextFlag());
						Thread t = new Thread(new Runnable() {
						    @Override
						    public void run() {
						    	Plot2 plot2 = new Plot2("Results", 1024, 512);
								plot2.plot(mel.getRef(), (float) 0.07, 0, false, Color.RED);
								plot2.plot(mel.getRec(), (float) 0.07, 0, true, Color.BLUE); 								
						    }
						    
						   });
						t.start();
					}//end actionPerformed
				}//end ActionListener
				);


		JPanel panel = new JPanel();
		panel.setBounds(6, 164, 738, 224);
		frame.getContentPane().add(panel);

		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setBounds(6, 427, 70, 16);
		frame.getContentPane().add(lblUsername);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(0, 449, 56, 29);
		frame.getContentPane().add(btnSave);

		JLabel lblLevel_1 = new JLabel("Level :");
		lblLevel_1.setBounds(406, 34, 61, 16);
		frame.getContentPane().add(lblLevel_1);

		JLabel lblExercise = new JLabel("Exercise :");
		lblExercise.setBounds(406, 62, 61, 16);
		frame.getContentPane().add(lblExercise);


		lblA.setBounds(471, 34, 44, 16);
		frame.getContentPane().add(lblA);


		label.setBounds(471, 62, 44, 16);

		frame.getContentPane().add(label);

		JLabel lblAsd = new JLabel(textField.getText());
		lblAsd.setBounds(88, 427, 61, 16);
		frame.getContentPane().add(lblAsd);

	}

	private void mainWindow2() throws InvalidMidiDataException, IOException {
		this.setTitle("Rhytmic Trainer");

		frame = new JFrame();
		frame.setBounds(100, 100, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);


		JLabel exer = new JLabel("Exercise Info");
		exer.setFont(new Font("SansSerif",Font.BOLD, 18));
		exer.setBounds(406, 6, 140, 16);
		frame.getContentPane().add(exer);

		ButtonGroup buttonGroup = new ButtonGroup();

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 115, 738, 12);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 415, 738, 16);
		frame.getContentPane().add(separator_1);

		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(382, 6, 12, 97);
		frame.getContentPane().add(separator_3);

		JButton playButton = new JButton("Play");
		playButton.setBounds(6, 16, 62, 29);
		frame.getContentPane().add(playButton);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					rit.playEx();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		JButton btnStartRec = new JButton("Start Rec");
		JButton btnRecord = new JButton("Finish Rec");
		btnRecord.setBounds(185, 16, 93, 29);
		frame.getContentPane().add(btnRecord);
		btnRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnStartRec.setEnabled(true);
				btnRecord.setEnabled(false);

				rit.getTargetDataLine().stop();
				rit.getTargetDataLine().close();

			}
		});

		btnStartRec.setBounds(80, 16, 93, 29);
		frame.getContentPane().add(btnStartRec);
		btnStartRec.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnStartRec.setEnabled(false);
				btnRecord.setEnabled(true);
				rit.captureAudio();
			}
		});

		JButton prev = new JButton("< Previous");
		prev.setBounds(406, 90, 117, 29);
		prev.setEnabled(true);
		frame.getContentPane().add(prev);

		JButton next = new JButton("Next >");
		next.setBounds(535, 90, 117, 29);
		next.setEnabled(rit.isNextFlag());
		frame.getContentPane().add(next);

		prev.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						int exNum = rit.getExNo();
						rit.setExNo(exNum-1);
						exer.setText(""+(exNum-1));
					}//end actionPerformed
				}//end ActionListener
				);
		next.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						int exNum = rit.getExNo();
						rit.setExNo(exNum+1);
						mel.getExGen().setExNum(exNum+1);
						exer.setText(""+(exNum+1));
					}//end actionPerformed
				}//end ActionListener
				);

		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setBounds(290, 16, 80, 29);
		frame.getContentPane().add(btnAnalyze);
		btnAnalyze.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						rit.analyze();
						next.setEnabled(rit.isNextFlag());
						Thread t = new Thread(new Runnable() {
						    @Override
						    public void run() {
						    	Plot2 plot2 = new Plot2("Results", 1024, 512);
								plot2.plotInt3(rit.getAl1(),(float) 1, 0, false, Color.RED);
								plot2.plotInt3(rit.getAl2(),(float) 1, 1, true, Color.BLUE);
								
						    }
						    
						   });
						t.start();
					}//end actionPerformed
				}//end ActionListener
				);


		JPanel panel = new JPanel();
		panel.setBounds(6, 164, 738, 224);
		frame.getContentPane().add(panel);

		JLabel lblUsername = new JLabel("Username :");
		lblUsername.setBounds(6, 427, 70, 16);
		frame.getContentPane().add(lblUsername);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(0, 449, 56, 29);
		frame.getContentPane().add(btnSave);

		JLabel lblLevel_1 = new JLabel("Level :");
		lblLevel_1.setBounds(406, 34, 61, 16);
		frame.getContentPane().add(lblLevel_1);

		JLabel lblExercise = new JLabel("Exercise :");
		lblExercise.setBounds(406, 62, 61, 16);
		frame.getContentPane().add(lblExercise);

		JLabel lblA = new JLabel(String.valueOf(rit.getLevel()));
		lblA.setBounds(471, 34, 44, 16);
		frame.getContentPane().add(lblA);

		JLabel label = new JLabel(String.valueOf(rit.getExNo()));
		label.setBounds(471, 62, 44, 16);
		frame.getContentPane().add(label);



		JLabel lblAsd = new JLabel(textField.getText());
		lblAsd.setBounds(88, 427, 61, 16);
		frame.getContentPane().add(lblAsd);





	}

	public static void test() {
		JSONArray arr = new JSONArray();
		
		JSONObject obj = new JSONObject();
		obj.put("name", textField.getText());

		JSONArray melo = new JSONArray();
		melo.add("1");
		melo.add("1");

		JSONArray ritm = new JSONArray();
		ritm.add("1");
		ritm.add("2");

		obj.put("rithymic", ritm);
		obj.put("melodic", melo);
		
		arr.add(obj);
		try {

			FileWriter file = new FileWriter("test.json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/*public static void saveMelodic() {
		JSONParser parser = new JSONParser();
		boolean oldu = false;
		try {  

			Object obj = parser.parse(new FileReader("test.json"));  
			JSONArray jsonArray = (JSONArray) obj;
			int length = jsonArray.size();
			for (int i =0; i< length; i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if(textField.getText().compareTo(jsonObject.get("name").toString())==0){
					oldu = true;
					JSONArray melArray = (JSONArray) jsonObject.get("melodic");
					//melArray.set(0, );
					//melArray.set(1, );
					
				}
			}
			
			if(oldu==false){
				JSONArray arr = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				
				JSONObject new = new JSONObject();
				obj.put("name", "kaka");
				JSONArray melo = new JSONArray();
				melo.add("1");
				melo.add("1");
				JSONArray ritm = new JSONArray();
				ritm.add("1");
				ritm.add("2");
				obj.put("rithymic", ritm);
				obj.put("melodic", melo);
				
				arr.add(new);
				System.out.println("eee");
				
				try {

					FileWriter file = new FileWriter("test.json");
					file.write(arr.toJSONString());
					file.flush();
					file.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  

	}
	*/
	public void readMelodic() throws NumberFormatException, InvalidMidiDataException {
		JSONParser parser = new JSONParser(); 
		boolean oldu = false;
		try {  

			Object obj = parser.parse(new FileReader("test.json")); 
			
			JSONArray jsonArray = (JSONArray) obj;
			int length = jsonArray.size();
			for (int i =0; i< length; i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if(textField.getText().compareTo(jsonObject.get("name").toString())==0){
					oldu = true;
					JSONArray melArray = (JSONArray) jsonObject.get("melodic");
					
					this.mel = new MelodicMain(Integer.parseInt(melArray.get(0).toString()),Integer.parseInt(melArray.get(1).toString()));
					
					System.out.println("BASARDIM!!!!!!!!!!!");
					System.out.println(melArray);
					System.out.println(melArray.get(0));
		
				}
			}

			if(oldu==false) {
				this.mel = new MelodicMain(1,1);
			}

		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  

		//
	}
	public void readRhytmic() throws NumberFormatException, InvalidMidiDataException {
		JSONParser parser = new JSONParser(); 
		boolean oldu = false;
		try {  

			Object obj = parser.parse(new FileReader("test.json"));  
			JSONArray jsonArray = (JSONArray) obj;
			int length = jsonArray.size();
			for (int i =0; i< length; i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				if(textField.getText().compareTo(jsonObject.get("name").toString())==0){
					oldu = true;
					JSONArray melArray = (JSONArray) jsonObject.get("rhytmic");
					
					this.rit = new RhythmicMain(Integer.parseInt(melArray.get(1).toString()));
					
					System.out.println("BASARDIM!!!!!!!!!!!");
					System.out.println(melArray);
					System.out.println(melArray.get(0));
		
				}
			}

			if(oldu==false) {
				this.rit = new RhythmicMain(1);
			}

		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} catch (ParseException e) {  
			e.printStackTrace();  
		}  

		//
	}




}





