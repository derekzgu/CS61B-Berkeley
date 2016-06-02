/**
 * Created by chizhang on 6/2/16.
 */
public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private int accumulate;
    private double accelerate;

    public AcceleratingSawToothGenerator(int period, double accelerate) {
        this.period = period;
        this.accelerate = accelerate;
        state = 0;
        accumulate = 0;
    }

    @Override
    public double next() {
        state += 1;
        if ((state - accumulate) % period == 0) {
            accumulate += period;
            period = (int) (period * accelerate); // rounded down
        }
        return normalize();
    }

    private double normalize() {
        return ((double)(state - accumulate)) / (double) period * 2.0 - 1.0;
    }

    public static void main(String[] args) {
        Generator generator = new AcceleratingSawToothGenerator(200, 1.1);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
}
