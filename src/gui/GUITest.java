package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import melodic.MelodicMain;

public class GUITest extends JFrame{
	
	MelodicMain mel;
	private JButton but1 = new JButton("Play");
	private JButton but2 = new JButton("Start Rec");
	private JButton but3 = new JButton("Finish Rec");
	private JButton but4 = new JButton("Analyze");
	
	public GUITest() throws InvalidMidiDataException, IOException{
		this.mel = new MelodicMain();
		this.setLayout(new GridLayout(0, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Singing Trainer");
		this.but1.setEnabled(true);
		this.but2.setEnabled(true);
		this.but3.setEnabled(false);
		this.but4.setEnabled(true);		

		JPanel notes = new ImagePanel();
		notes.setBorder(new TitledBorder("Score"));
		
		JPanel buttons = new JPanel();
		buttons.setBorder(new TitledBorder("Control Panel"));
		buttons.setSize(100,100);
		but1.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						mel.getExGen().playEx();
						//Capture input data from the
						// microphone until the Stop button is
						// clicked.
					}//end actionPerformed
				}//end ActionListener
				);
		but2.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						but2.setEnabled(false);
						but3.setEnabled(true);
						//Capture input data from the
						// microphone until the Stop button is
						// clicked.
						mel.captureAudio();
					}//end actionPerformed
				}//end ActionListener
				);
		but3.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						but2.setEnabled(true);
						but3.setEnabled(false);
						//Terminate the capturing of input data
						// from the microphone.
						mel.getTargetDataLine().stop();
						mel.getTargetDataLine().close();
					}//end actionPerformed
				}//end ActionListener
				);
		but4.addActionListener(
				new ActionListener(){
					public void actionPerformed(
							ActionEvent e){
						mel.analyze();
					}//end actionPerformed
				}//end ActionListener
				);
		

		buttons.add(but1);
		buttons.add(but2);
		buttons.add(but3);
		buttons.add(but4);
		//this.add(buttons);

		JPanel info = new JPanel(new GridBagLayout());
		info.setBorder(new TitledBorder("Exercise Info"));
		JLabel level = new JLabel("Level 3");
		level.setBorder(new TitledBorder(""));
		JLabel exer = new JLabel("Ex. 9");
		exer.setBorder(new TitledBorder(""));
		JLabel label = new JLabel("C-Major");
		label.setBorder(new TitledBorder(""));
		label.setFont(new Font("SansSerif", Font.BOLD, 20));
		JLabel empt = new JLabel(" ");
		JLabel empt1 = new JLabel(" ");
		JButton prev = new JButton("<Previous");
		prev.setEnabled(true);
		JButton next = new JButton("Next>");
		next.setEnabled(true);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.25;
		gc.weighty = 0.0;
		gc.fill = gc.BOTH;
		info.add(level, gc);
		gc.gridy++;
		info.add(exer, gc);
		gc.gridy++;
		info.add(empt,gc);
		gc.gridy++;
		info.add(label,gc);
		gc.gridy++;
		info.add(empt1,gc);
		gc.gridy++;
		info.add(prev,gc);
		gc.gridx++;
		info.add(next,gc);

		JPanel containerPanel = new JPanel(new GridLayout(1,0));
		containerPanel.add(buttons);
		containerPanel.add(info);
		this.add(containerPanel,BorderLayout.NORTH);
		this.add(notes);

	}
	
	class ImagePanel extends JPanel{

		private BufferedImage image;

		public ImagePanel() {
			try {                
				image = ImageIO.read(new File("NOTES3.png"));
			} catch (IOException ex) {
				// handle exception...
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, (this.getWidth()-image.getWidth())/2, (this.getHeight()-image.getHeight())/2, null); // see javadoc for more info on the parameters     
			int i=0;
			g.drawRect(300+(41*i), (this.getHeight()-image.getHeight()+10)/2, 20, image.getHeight()-10);
		}

	}
	
	public static void main(String args[]) throws InvalidMidiDataException, IOException{
		GUITest test = new GUITest();
		
		test.pack();
		test.setSize(900, 500);
		test.setVisible(true);
	}
}
