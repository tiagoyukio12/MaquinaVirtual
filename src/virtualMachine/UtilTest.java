package virtualMachine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void evaluateIntToWord() {
        // Random numbers
        assertEquals("ffff", Util.intToWord(-1));
        assertEquals("fff1", Util.intToWord(-15));
        assertEquals("cfc7", Util.intToWord(-12345));
        assertEquals("109e", Util.intToWord(4254));
        assertEquals("0001", Util.intToWord(1));
        assertEquals("0000", Util.intToWord(0));
        // Max and min values
        assertEquals("7fff", Util.intToWord(32767));
        assertEquals("8000", Util.intToWord(-32768));
    }

    @Test
    public void evaluateWordToInt() {
        assertEquals(-1, Util.wordToInt("ffff"));
        assertEquals(-15, Util.wordToInt("fff1"));
        assertEquals(-12345, Util.wordToInt("cfc7"));
        assertEquals(4254, Util.wordToInt("109e"));
        assertEquals(1, Util.wordToInt("0001"));
        assertEquals(0, Util.wordToInt("0000"));

        assertEquals(-32768, Util.wordToInt("8000"));
        assertEquals(32767, Util.wordToInt("7fff"));

        // Reading bytes as unsigned values
        assertEquals(255, Util.wordToInt("ff"));
        assertEquals(127, Util.wordToInt("7f"));
    }

    @Test
    public void evaluateSignedToUnsigned() {
        assertEquals(61440, Util.signedToUnsigned(-4096));
        assertEquals(61440, Util.signedToUnsigned(61440));
        assertEquals(0, Util.signedToUnsigned(0));
        assertEquals(65535, Util.signedToUnsigned(-1));
    }
}