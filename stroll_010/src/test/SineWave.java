package test;

public class SineWave extends Waveform<SineWave> {
    /**
     * SINE_TABLE_SIZE = 1024 (1<<10)
     */
    private final static int SINE_TABLE_SIZE = 1 << 10;

    /**
     * stores a table of `SINE_TABLE_SIZE` sines in .16
     */
    private final static double[] sineTable = createSineTable(SINE_TABLE_SIZE);

    protected final static double[] createSineTable(int size) {
        double[] sineTable = new double[size];

        for (int i = 0; i < size; i++) {
            double radians = (double) (i) * (2 * Math.PI) / (double) size;
            sineTable[i] = Math.sin(radians);
        }
        return sineTable;
    }

    public SineWave() {
        super();
    }

    public SineWave(double frequency, double sampleRate, double phase) {
        super(frequency, sampleRate, phase);
    }

    public SineWave createInstance(double frequency, double sampleRate,
                                   double phase) {
        return new SineWave(frequency, sampleRate, phase);
    }

    public double getAmplitude() {
        // obtain a usable phase value to index the table of sines
        int phaseValue = (int) (this.phase * (1 << 16));
        if (phaseValue < 0)
            phaseValue %= SINE_TABLE_SIZE;
        if (phaseValue < 0)
            phaseValue += SINE_TABLE_SIZE;
        phaseValue &= 0xffff;

        // phaseValue is .16, sineTable index is .10
        return sineTable[phaseValue >> 6];
    }
}
