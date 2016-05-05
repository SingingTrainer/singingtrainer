package rhythmic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import utilities.AudioUtilities;
import utilities.ThresholdFunction;
import utilities.WaveReader;


public class RhythmicMain {

	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	public static final int HISTORY_SIZE = 50;
	public static final float[] multipliers = { 2f, 2f, 2f };
	ArrayList<Integer> peakPro;
	int[] a1; 
	int[] a2;
	double[] a3;
	double[] a4;
	int numOfPeaks;
	double totalMeas;
	ArrayList<Integer> locOfPeaks;
	double dist;
	String exName;
	ArrayList<Integer> al1 = new ArrayList<Integer>();
	ArrayList<Integer> al2 = new ArrayList<Integer>();

	public RhythmicMain(){
		exName = "rhy1";

	}

	public void captureAudio(){
		try{
			//Get things set up for capture
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo =
					new DataLine.Info(
							TargetDataLine.class,
							audioFormat);
			targetDataLine = (TargetDataLine)
					AudioSystem.getLine(dataLineInfo);

			//Create a thread to capture the microphone
			// data into an audio file and start the
			// thread running.  It will run until the
			// Stop button is clicked.  This method
			// will return after starting the thread.
			new CaptureThread().start();
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}//end catch
	}

	public AudioFormat getAudioFormat(){
		float sampleRate = 44100F;
		//8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		//8,16
		int channels = 1;
		//1,2
		boolean signed = true;
		//true,false
		boolean bigEndian = false;
		//true,false
		return new AudioFormat(sampleRate,
				sampleSizeInBits,
				channels,
				signed,
				bigEndian);
	}

	class CaptureThread extends Thread{
		public void run(){
			AudioFileFormat.Type fileType = null;
			File audioFile = null;

			//Set the file type and the file extension
			// based on the selected radio button.

			fileType = AudioFileFormat.Type.WAVE;
			audioFile = new File("recRhy.wav");
			//end if

			try{
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(
						new AudioInputStream(targetDataLine),
						fileType,
						audioFile);
			}catch (Exception e){
				e.printStackTrace();
			}//end catch

		}//end run
	}
	
	public void playEx() throws IOException{
		String gongFile = this.exName.concat(".wav");
	    InputStream in = new FileInputStream(gongFile);
	 
	    // create an audiostream from the inputstream
	    AudioStream audioStream = new AudioStream(in);
	 
	    // play the audio clip with the audioplayer class
	    AudioPlayer.player.start(audioStream);
	}

	public void analyze(){
		RhythmicHelper rh1 = new RhythmicHelper(this.exName);
		RhythmicHelper rh2 = new RhythmicHelper("recRhy");
		
		double totalM1 = rh1.getTotalMeas();
		double totalM2 = rh2.getTotalMeas();
		
		int base1 = (int) (400/totalM1);
		int base2 = (int) (400/totalM2);
		
		int peak1 = rh1.getA4().length;
		int peak2 = rh2.getA4().length;
		
		int[] ref1 = new int[400];
		int[] ref2 = new int[400];
		
		double[] peaks1 = rh1.getA4();
		double[] peaks2 = rh2.getA4();
		
		ref1[0]=1;
		for(int i=0;i<peak1-1;i++){
			double num=0;
			for(int j=0; j<=i;j++){
				num=num+peaks1[j];
			}
			ref1[(int) (num*base1)]=1;
		}
		ref2[0]=1;
		for(int i=0;i<peak2-1;i++){
			double num=0;
			for(int j=0; j<=i; j++){
				num=num+peaks2[j];
			}
			ref2[(int) (num*base2)]=1;
		}
		
		for(int i=0;i<ref1.length;i++){
			this.al1.add(ref1[i]);
		}
		
		for(int i=0;i<ref2.length;i++){
			this.al2.add((int) ref2[i]);
		}
	}

	public ArrayList<Integer> getAl1() {
		return al1;
	}

	public ArrayList<Integer> getAl2() {
		return al2;
	}
	
	
}
