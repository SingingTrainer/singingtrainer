package rhythmic;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.Plot2;
import utilities.AudioUtilities;
import utilities.ThresholdFunction;
import utilities.WaveReader;

public class RhythmicHelper {
	
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

	public RhythmicHelper(String name){
		WaveReader reader = new WaveReader();
		File soundFile = new File(name.concat(".wav"));
		reader.changeWave(soundFile);
		float[] org=reader.getFloatData();

		float[][] chunked = AudioUtilities.chunkArray(org,1024);

		FFT fft = new FFT( 1024, 44100 );
		fft.window( FFT.HAMMING );
		float[] samples = new float[1024];
		float[] spectrum = new float[1024 / 2 + 1];
		float[] lastSpectrum = new float[1024 / 2 + 1];
		List<Float> spectralFlux = new ArrayList<Float>( );
		List<Float> hop = new ArrayList<Float>();

		//BufferedWriter wr = new BufferedWriter(new FileWriter(new File("out2.txt")));
		for(int j=0;j<chunked.length;j++){
			//while( decoder.readSamples( samples ) > 0 )
			//{			
			fft.forward( chunked[j] );
			System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length ); 
			System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );

			float flux = 0;
			for( int i = 0; i < spectrum.length; i++ )	
			{
				float value = (spectrum[i] - lastSpectrum[i]);
				flux += value < 0? 0: value;
			}
			spectralFlux.add( flux );
			hop.add(flux*11/12);
			//}		
		}
		List<List<Float>> thresholds = new ArrayList<List<Float>>( );

		List<Float> threshold = new ThresholdFunction( HISTORY_SIZE, multipliers[0]).calculate( spectralFlux );
		thresholds.add( threshold );
		
		/*Thread t = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	Plot2 plot = new Plot2( "Spectral Flux", 1024, 512 );
				plot.plot(spectralFlux, 1, 0, false, Color.red);
				plot.plot(thresholds.get(0), 1,0,true,Color.blue);								
		    }
		    
		   });
		t.start();*/

		/*Plot2 plot = new Plot2( "Spectral Flux", 1024, 512 );
		plot.plot(spectralFlux, 1, 0, false, Color.red);
		plot.plot(thresholds.get(0), 1,0,true,Color.blue);*/
		ArrayList<Integer> peak = new ArrayList<Integer>();
		this.peakPro = new ArrayList<Integer>();
		for(int i=0;i<spectralFlux.size();i++){
			if(spectralFlux.get(i)>thresholds.get(0).get(i)+2){
				peak.add(1);
			}else{
				peak.add(0);
			}
		}
		int count =0;
		for(int i=0;i<peak.size();i++){
			if(count==0 && peak.get(i)==1){
				count++;
			}
			if(count==1 && peak.get(i)==1){
				for(int j=0;j<peak.size();j++){
					if((i+j)<peak.size())
						peakPro.add(peak.get(i+j));
				}
				count++;
			}
		}
		a1 = new int[peakPro.size()];
		
		for(int i=0; i<peakPro.size();i++){
			a1[i]=peakPro.get(i);
		}
		this.numOfPeaks=0;
		locOfPeaks = new ArrayList<Integer>();
		int flag=0;
		for(int i=0;i<a1.length;i++){
			if(i!=0){
				if(a1[i]==1 && a1[i-1]!=1 && a1[i-2]!=1){
					numOfPeaks++;
					locOfPeaks.add(i);
					System.out.println(i);
				}
			}else{
				if(a1[i]==1){
					numOfPeaks++;
					locOfPeaks.add(i);
					System.out.println(i);
				}
			}
				
			if(a1[i]==1)
				flag=i;
		}
		System.out.println("hop");
		
		a2 = new int[flag+1];
		a2[0]=1;
		for(int i=0; i<a2.length;i++){
			a2[i]=a1[i];
		}
		
		a3 = new double[numOfPeaks-1];
		for(int i=0; i<numOfPeaks-1;i++){
			a3[i]=locOfPeaks.get(i+1)-locOfPeaks.get(i);
		}
		totalMeas=0;
		a4 = new double[a3.length];
		for(int i=0;i<a3.length;i++){
			double first = a3[0];
			a4[i]=a3[i]/first;
			totalMeas=totalMeas+a4[i];
		}
	}

	public double[] getA4() {
		return a4;
	}

	public int getNumOfPeaks() {
		return numOfPeaks;
	}

	public double getTotalMeas() {
		return totalMeas;
	}
	
	
}
