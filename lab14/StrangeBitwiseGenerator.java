/**
 * Created by chizhang on 6/2/16.
 */
public class StrangeBitwiseGenerator implements Generator{
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state += 1;
        int weirdState = state & (state >> 7) % period;
        return normalize(weirdState);
    }

    private double normalize(int state) {
        return (double)(state) / (double) period * 2.0 - 1.0;
    }

    public static void main(String[] args) {
        Generator generator = new StrangeBitwiseGenerator(1024);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(128000, 1000000);
    }
}
