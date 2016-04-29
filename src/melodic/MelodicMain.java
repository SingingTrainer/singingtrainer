package melodic;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

import utilities.AudioUtilities;
import utilities.WaveReader;


public class MelodicMain {

	private int level;
	private int exNo;
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private Yin yin;
	private float[] ref, rec, orgPitchResult, org;
	private float sampleRate;
	private int bufferSize;
	private ExerciseGen exGen;
	private DTW dtw;
	private double dist;

	public MelodicMain() throws InvalidMidiDataException, IOException{
		this.level=1;
		this.exNo=1;
		this.exGen = new ExerciseGen(exNo);

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
			audioFile = new File("rec.wav");
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
	
	public void analyze(){
		this.ref = exGen.getRefArr();
		estimate(this.org);
		rec = orgPitchResult;
		dtw = new DTW(ref,rec);
		dist = dtw.getDistance();
		checkResult();
	}
	
	public void checkResult(){
		if(dist<12000){
			JOptionPane.showMessageDialog(null, "You have failed this exercise, try again!");
		}else{
			JOptionPane.showMessageDialog(null, "Congrats! Do the next one!");
			exNo=exNo+1;
			if(exNo>16){
				exNo=1;
				level=level+1;
			}
		}
	}
	
	public void readWaveFile(){
		WaveReader reader = new WaveReader();
		File soundFile = new File("rec.wav");
		reader.changeWave(soundFile);
		this.org=reader.getFloatData();
		this.sampleRate = reader.getSampleRate();
		this.bufferSize = 2048;
	}
	
	public void estimate(float[] arr){
		float[][] chunked = AudioUtilities.chunkArray(arr,bufferSize);
		yin = new Yin(sampleRate,bufferSize);
		orgPitchResult = new float[chunked.length];
		for (int i=0; i<orgPitchResult.length;++i){
			float r = yin.getPitch(chunked[i]).getPitch();

			while((r<250 || r>515) && r!=-1){
				if(r<250)
					r=r*2;
				if(r>515)
					r=r/2;
			}

			if(r==-1){
				orgPitchResult[i] = 0;
			}
			else{
				orgPitchResult[i] = r;
			}
		}
	}

	public float[] getRef() {
		return ref;
	}

	public float[] getRec() {
		return rec;
	}

	public ExerciseGen getExGen() {
		return exGen;
	}

	public DTW getDtw() {
		return dtw;
	}

	public TargetDataLine getTargetDataLine() {
		return targetDataLine;
	}
	
	

}