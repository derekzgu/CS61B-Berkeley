/**
 * Created by chizhang on 6/2/16.
 */
public class SawToothGenerator implements Generator {

    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state += 1;
        return normalize();
    }

    private double normalize() {
        return ((double)(state % period)) / (double) period * 2.0 - 1.0;
    }

    public static void main(String[] args) {
        Generator generator = new SawToothGenerator(512);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
}
