package melodic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;

import org.jfugue.midi.MidiParser;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Note;


public class ExerciseGen {

	ArrayList<Integer> arrLst = new ArrayList<Integer>();
	int[] pos;
	public ArrayList<Double> notes = new ArrayList<Double>();
	float[] refArr;
	public ArrayList<Byte> notesByLst = new ArrayList<Byte>();
	byte[] notesBy;
	float[] ex;
	int[] exPos;

	int exNum;

	public int[] getExPos() {
		return exPos;
	}

	public void setExPos(int[] exPos) {
		this.exPos = exPos;
	}

	public ExerciseGen(int exNum) throws InvalidMidiDataException, IOException{
		MidiParser parser = new MidiParser(); // Remember, you can use any Parser!
		MyParserListener listener = new MyParserListener();
		parser.addParserListener(listener);
		parser.parse(MidiSystem.getSequence(new File("C_major2.mid")));
		notes=listener.getArrLst();
		notesByLst=listener.getByteLst();
		this.exNum=exNum;
		createEx();
	}

	public void createRefArr(float[] arrLst){
<<<<<<< HEAD
		refArr = new float[44100*3/2048];
		int rep = refArr.length/arrLst.length;
=======
		refArr = new float[44100*5/2048];
		int rep = refArr.length/arrLst.length+1;
>>>>>>> a7d40595e2811816278b318cc9a8a59eb1652fb7
		for(int i=0;i<refArr.length;i++){
			refArr[i]=arrLst[i/rep];
		}
	}

	public void createExArr(){
		refArr = new float[44100/2048*3];

		for(int i=0;i<refArr.length;i++){
			refArr[i]=ex[i/(refArr.length/ex.length)];
		}
	}

	public void createEx(){
		if(exNum<5){
			exPos=create1Ex();
			ex= new float[1];
			ex[0]=notes.get(exPos[0]).floatValue();
		}else if(exNum>=5 && exNum<9){
			exPos=create2Ex();
			ex= new float[2];
			for(int i=0;i<2;i++){
				ex[i]=notes.get(exPos[i]).floatValue();
			}
		}else if(exNum>=9 && exNum<13){
			exPos=create3Ex();
			ex=new float[3];
			for(int i=0;i<3;i++){
				ex[i]=notes.get(exPos[i]).floatValue();
			}
		}else{
			exPos=create4Ex();
			ex=new float[4];
			for(int i=0;i<4;i++){
				ex[i]=notes.get(exPos[i]).floatValue();
			}
		}

		createRefArr(ex);
	}

	public void playEx(){
		Pattern pattern = new Pattern();
		pattern.setTempo(100);
		for(int i=0;i<ex.length;i++){
			Note note = new Note(notesByLst.get(exPos[i]),1);
			pattern.add(note);
		}

		Player player = new Player();
		player.play(pattern);
	}

	public int[] create1Ex(){
		int i=(int) (Math.random()*notes.size());
		int [] ex = new int[1];

		ex[0]=i;
		return ex;

	}

	public int[] create2Ex(){
		int[] ex = new int[2];
		ex[0]=0;
		ex[1]=((int) (Math.random()*notes.size()));

		return ex;
	}

	public int[] create3Ex(){
		int[] ex = new int[3];
		int root = ((int) (Math.random()*notes.size()));
		ex[0]=root;
		for(int i=1;i<3;i++){
			ex[i]=(root+(i*2))%7;
		}

		return ex;
	}

	public int[] create4Ex(){
		int[] ex = new int[4];
		int root = ((int) (Math.random()*notes.size()));
		ex[0]=root;
		for(int i=1;i<4;i++){
			ex[i]=(root+(i*2))%7;
		}

		return ex;
	}


	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		ExerciseGen prs = new ExerciseGen(7);

	}

	public float[] getRefArr() {
		return refArr;
	}

	public void setExNum(int exNum) {
		this.exNum = exNum;
		createEx();
	}

	public int getExNum() {
		return exNum;
	}
	
}

class MyParserListener extends ParserListenerAdapter {

	ArrayList<Integer> arrLst = new ArrayList<Integer>();
	int[] pos;
	public ArrayList<Double> notes = new ArrayList<Double>();
	float[] refArr;
	public ArrayList<Byte> notesByLst = new ArrayList<Byte>();

	@Override
	public void onNoteParsed(Note note) {
		// A "C" note is in the 0th position of an octave
		if(Note.getFrequencyForNote(note.getValue())>10){
			notes.add(Note.getFrequencyForNote(note.getValue()));
			notesByLst.add(note.getValue());
			//System.out.println(Note.getFrequencyForNote(note.getValue()));
		}
	}

	public ArrayList<Byte> getByteLst(){
		return notesByLst;
	}

	public ArrayList<Double> getArrLst(){
		return notes;
	}
}