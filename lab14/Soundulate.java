import java.util.ArrayList;

public class Soundulate {

    public static void main(String[] args) {
        Generator generator = new SineWaveGenerator(200);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);
    }
} 