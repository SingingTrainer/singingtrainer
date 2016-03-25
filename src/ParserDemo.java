import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;

import org.jfugue.midi.MidiParser;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.theory.Note;

public class ParserDemo {
    public static void main(String[] args) throws InvalidMidiDataException, IOException {
        MidiParser parser = new MidiParser(); // Remember, you can use any Parser!
        MyParserListener listener = new MyParserListener();
        parser.addParserListener(listener);
        parser.parse(MidiSystem.getSequence(new File("B_major.mid")));
        //System.out.println("There are " + listener.counter + " 'C' notes in this music.");
    }
}

class MyParserListener extends ParserListenerAdapter {
    public int counter=0;

    @Override
    public void onNoteParsed(Note note) {
        // A "C" note is in the 0th position of an octave
    	if(Note.getFrequencyForNote(note.getValue())>10)
    		System.out.println(Note.getFrequencyForNote(note.getValue()));
        if (note.getPositionInOctave() == 5) {
            counter++;
        }
    }
}