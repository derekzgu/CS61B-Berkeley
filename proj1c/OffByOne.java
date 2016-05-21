/**
 * Created by chizhang on 5/20/16.
 */
import org.junit.Test;
import static org.junit.Assert.*;

public class OffByOne implements CharacterComparator {

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }

    @Test
    public void testEqualChars() {
        char x = 'a';
        char y = 'b';
        assertTrue(equalChars(x, y));
        x = 'r'; y = 'q';
        assertTrue(equalChars(x, y));
        assertTrue(!equalChars('a', 'e'));
        assertTrue(!equalChars('z', 'a'));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", OffByOne.class);
    }
}
