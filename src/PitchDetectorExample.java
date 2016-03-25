/*
*      _______                       _____   _____ _____  
*     |__   __|                     |  __ \ / ____|  __ \ 
*        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
*        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
*        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
*                                                         
* -------------------------------------------------------------
*
* TarsosDSP is developed by Joren Six at IPEM, University Ghent
*  
* -------------------------------------------------------------
*
*  Info: http://0110.be/tag/TarsosDSP
*  Github: https://github.com/JorenSix/TarsosDSP
*  Releases: http://0110.be/releases/TarsosDSP/
*  
*  TarsosDSP includes modified source code by various authors,
*  for credits and info, see README.
* 
*/



import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.example.InputPanel;
import be.tarsos.dsp.example.PitchDetectionPanel;
import be.tarsos.dsp.example.Shared;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class PitchDetectorExample extends JFrame implements PitchDetectionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3501426880288136245L;

	private final JTextArea textArea;
	private JButton but1 = new JButton("Play");
	private JButton but2 = new JButton("Stop");
	private JButton but3 = new JButton("Repeat");
	private JButton but4 = new JButton("Record");
	private int flag=0;
	private ArrayList<Float> arrayLst = new ArrayList<Float>();
	

	private AudioDispatcher dispatcher;
	private Mixer currentMixer;
	
	private PitchEstimationAlgorithm algo;	
	private ActionListener algoChangeListener = new ActionListener(){
		@Override
		public void actionPerformed(final ActionEvent e) {
			String name = e.getActionCommand();
			PitchEstimationAlgorithm newAlgo = PitchEstimationAlgorithm.valueOf(name);
			algo = newAlgo;
			try {
				setNewMixer(currentMixer);
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			}
	}};

	public PitchDetectorExample() {
		this.setLayout(new GridLayout(0, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Singing Trainer");
		this.but1.setEnabled(true);
		this.but1.addActionListener(new But1());
		this.but2.setEnabled(true);
		this.but3.setEnabled(true);
		this.but4.setEnabled(true);
		
		JPanel inputPanel = new InputPanel();
		
		inputPanel.addPropertyChangeListener("mixer",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						try {
							setNewMixer((Mixer) arg0.getNewValue());
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedAudioFileException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		
		algo = PitchEstimationAlgorithm.YIN;
		JPanel notes = new ImagePanel();
		notes.setBorder(new TitledBorder("Score"));
		
		
		JPanel buttons = new JPanel();
		buttons.setBorder(new TitledBorder("Control Panel"));
		buttons.setSize(100,100);
		but1.addActionListener(new But1());
		but2.addActionListener(new But2());
		but3.addActionListener(new But3());
		
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
		containerPanel.add(inputPanel);
		containerPanel.add(buttons);
		containerPanel.add(info);
		this.add(containerPanel,BorderLayout.NORTH);
		this.add(notes);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		add(new JScrollPane(textArea));
	}


	
	private void setNewMixer(Mixer mixer) throws LineUnavailableException,
			UnsupportedAudioFileException {
		
		if(dispatcher!= null){
			dispatcher.stop();
		}
		currentMixer = mixer;
		
		float sampleRate = 44100;
		int bufferSize = 1024;
		int overlap = 0;
		
		textArea.append("Started listening with " + Shared.toLocalString(mixer.getMixerInfo().getName()) + "\n");

		final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
				true);
		final DataLine.Info dataLineInfo = new DataLine.Info(
				TargetDataLine.class, format);
		TargetDataLine line;
		line = (TargetDataLine) mixer.getLine(dataLineInfo);
		final int numberOfSamples = bufferSize;
		line.open(format, numberOfSamples);
		line.start();
		final AudioInputStream stream = new AudioInputStream(line);

		JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
		// create a new dispatcher
		dispatcher = new AudioDispatcher(audioStream, bufferSize,
				overlap);

		// add a processor
		dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));
		
		new Thread(dispatcher,"Audio dispatching").start();
	}

	public static void main(String... strings) throws InterruptedException,
			InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					//ignore failure to set default look en feel;
				}
				JFrame frame = new PitchDetectorExample();
				frame.pack();
				frame.setSize(750, 350);
				frame.setVisible(true);
			}
		});
	}


	@Override
	public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
		if(pitchDetectionResult.getPitch() != -1 && flag==1){
			//float pitch = pitchDetectionResult.getPitch();
			double timeStamp = audioEvent.getTimeStamp();
			float pitch = pitchDetectionResult.getPitch();
			float probability = pitchDetectionResult.getProbability();
			double rms = audioEvent.getRMS() * 100;
			String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n", timeStamp,pitch,probability,rms);
			textArea.append(message);
			textArea.setCaretPosition(textArea.getDocument().getLength());
			while(pitch<250 || pitch> 520){
				if(pitch<250){
					pitch=pitch*2;
				}
				if(pitch>500){
					pitch=pitch/2;
				}
			}
			arrayLst.add(pitch);
			//System.out.println(pitch);
		}
	}
	
	private class But1 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			flag=1;
			
		}
		
	}
	
	private class But2 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			flag=0;
			
		}
		
	}
	
	private class But3 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			float[] refAr = {(float) 261.6, (float) 293.7,	(float) 329.6,	(float) 349.2, (float) 392.0, (float) 440.0	,(float) 493.9};
			float[] dobAr = new float[arrayLst.size()];
		
			for(int i=0;i<arrayLst.size();i++){
				dobAr[i]=arrayLst.get(i);
			}
			DTW dtw = new DTW(dobAr,refAr);
			System.out.println(dtw);
			for(int i=0;i<dobAr.length;i++){
				System.out.println(dobAr[i]);
			}
			
		}
		
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
	        g.drawImage(image, 175, 30, null); // see javadoc for more info on the parameters            
	    }

	}
	
}
