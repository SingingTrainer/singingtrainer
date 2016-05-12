package melodic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	private float[] ref, rec, orgPitchResult, org, newExp;
	private float sampleRate;
	private int bufferSize;
	private ExerciseGen exGen;
	private DTW dtw;
	private double dist;
	public boolean nextFlag;
	private float[] ex;

	
	public MelodicMain(int level, int exNo) throws InvalidMidiDataException, IOException{
		this.level=level;
		this.exNo=exNo;
		this.exGen = new ExerciseGen(exNo);
		this.nextFlag=false;
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
		readWaveFile();
		this.ref = exGen.getRefArr();
		this.ex = exGen.getEx();
		float max = getMax(ex);
		float min = getMin(ex);
		estimate(this.org, max, min);
		rec = orgPitchResult;
		dtw = new DTW(ref,rec);
		dist = dtw.getDistance()/rec.length;
		checkResult();
		resample(ref,rec);
	}
	
	public void checkResult(){
		
		if(dist>250){
			JOptionPane.showMessageDialog(null, "You have failed this exercise, try again!");
		}else{
			JOptionPane.showMessageDialog(null, "Congrats! Do the next one!");
			nextFlag=true;
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
	
	public void resample(float ref[], float[] rec){
		if(ref.length>rec.length){
			newExp = new float[ref.length];
			double div = ref.length/rec.length;
			int round = (int) Math.floor(div);
			int count=0;
			int level=0;
			int dif = ref.length-round*rec.length;

			for(int i=0;i<rec.length;i++){
				if(level<dif){
					round=(int) Math.floor(div)+1;
				}
				else{
					round=(int) Math.floor(div);
				}

				for(int j=0; j<round;j++){
					newExp[count]=rec[i];
					//System.out.println("NewExp");
					count++;

				}
				level++;

			}			
		}
		else{
			double div = rec.length/ref.length;
			int round = (int) Math.round(div);
			int count=0;
			int level=0;
			newExp = new float[ref.length];

			for(int i=0; i<rec.length;i++){

				if((i-level*(round-1))<newExp.length){
					if((count%round)==0){
						newExp[i-level*(round-1)]=rec[i];
						level++;
					}
				}

				count++;
			}
		}
		this.ref[0]=0;
	}
	
	public void estimate(float[] arr, float max, float min){
		float[][] chunked = AudioUtilities.chunkArray(arr,bufferSize);
		yin = new Yin(sampleRate,bufferSize);
		orgPitchResult = new float[chunked.length];
		
		for (int i=0; i<orgPitchResult.length;++i){
			float r = yin.getPitch(chunked[i]).getPitch();

			while(((r<(min/(Math.sqrt(2))) || r>(max*(Math.sqrt(2))))) && r!=-1){
				if(r<(min/(Math.sqrt(2))))
					r=r*2;
				if(r>(max*(Math.sqrt(2))))
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
	
	public float getMax(float[] ex){
		float max;
		ArrayList<Float> list = new ArrayList<Float>();

		for(int i=0; i<ex.length;i++){
			list.add(ex[i]);
		}
	    
		max = Collections.max(list);
		
		return max;
	}
	
	public float getMin(float[] ex){
		float min;
		ArrayList<Float> list = new ArrayList<Float>();

		for(int i=0; i<ex.length;i++){
			list.add(ex[i]);
		}
	    
		min = Collections.min(list);
		
		return min;
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

	public boolean isNextFlag() {
		return nextFlag;
	}

	public int getExNo() {
		return exNo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExNo(int exNo) {
		this.exNo = exNo;
	}

	public float[] getNewExp() {
		return newExp;
	}

	public void setNextFlag(boolean nextFlag) {
		this.nextFlag = nextFlag;
	}
	

}
