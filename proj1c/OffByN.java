/**
 * Created by chizhang on 5/20/16.
 */
public class OffByN implements CharacterComparator {

    private int charOffset;

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == charOffset;
    }

    public OffByN(int N) {
        charOffset = N;
    }
}
