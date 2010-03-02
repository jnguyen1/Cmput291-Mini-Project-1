/*
 * Keyboard Class:
 * The Keyboard class provides methods to read input from the user.
 *
 * Borrowed from CMPUT114 page.
 */
import java.io.*;

public class Keyboard {
/*
    An instance of this class represents a keyboard device that can be
    used to obtain input from the user.
*/

/* Public Variables */

public static final Keyboard in = new Keyboard();

/* Contructor */

    public Keyboard() {
    
    /*
        Initialize me.
    */
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
/* Instance Methods */

    public String readString() {
    /*
        Answer a String that contains all of the characters typed by the
        user until the enter key is pressed.
    */
        String aString;
        
        System.out.flush(); // Make sure all output is flushed
        try {
            aString = this.reader().readLine();
        } catch (Exception e) {
            aString = "";
        }
        return aString;
    }
    
    public Integer readInteger() {
    /*
        Answer an Integer that is represented by the String that 
        contains all of the characters typed by the user until the 
        enter key is pressed. If the text does not form a valid 
        Integer, then answer null.
    */
        
        String        aString;
        Integer        anInteger;
        
        System.out.flush(); // Make sure all output is flushed
        aString = this.readString();
        try {
            anInteger = new Integer(aString);
        } catch (Exception e) {
            anInteger = null;
        }
        return anInteger;
    }
    
    public Float readFloat() {
    /*
        Answer a Float that is represented by the String that 
        contains all of the characters typed by the user until the 
        enter key is pressed. If the text does not form a valid 
        Float, then answer null.
    */
        
        String        aString;
        Float        aFloat;
        
        System.out.flush(); // Make sure all output is flushed
        aString = this.readString();
        try {
            aFloat = new Float(aString);
        } catch (Exception e) {
            aFloat = null;
        }
        return aFloat;
    }
    
    public void pause() {
    /*
        Display a message and pause until the enter key is pressed.
    */
        String aString;
        
        System.out.print("Press the ENTER key to continue ...");
        System.out.flush(); // Make sure all output is flushed
        try {
            aString = this.reader().readLine();
        } catch (Exception e) {}
    }
    
/* Private Instance Variables */

    private BufferedReader reader;

/* Private Instance Methods - accessing */

    private BufferedReader reader() {
    /*
        Answer my reader.
    */
        
        return this.reader;
    }
    
    private void reader(BufferedReader aReader) {
    /*
        Set my reader to the given one.
    */
        
        this.reader = aReader;
    }
    
} 
