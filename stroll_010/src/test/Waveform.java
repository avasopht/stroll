package test;

public abstract class Waveform<DWaveform extends Waveform> {
    /**
     * frequency in hertz
     */
    protected final double frequency;
    /**
     * sampleRate in samples per second
     */
    protected final double sampleRate;
    /**
     * current phase, a phase of 1d is a complete cycle of the sineWave
     */
    protected final double phase;


    /**
     * creates a new Oscillator with a frequency of 1hz and 1 sample/sec
     */
    public Waveform() {
        this.frequency = 1d;
        this.sampleRate = 1d;
        this.phase = 0d;
    }

    /**
     * creates a new Oscillator with the given properties
     */
    protected Waveform(double frequency, double sampleRate, double phase) {
        this.frequency = frequency;
        this.sampleRate = sampleRate;
        this.phase = phase;
    }

    public abstract DWaveform createInstance(double frequency, double sampleRate,
                                             double phase);

    public DWaveform setPhase(double phase) {
        return createInstance(this.frequency, this.sampleRate, phase);
    }

    public DWaveform advanceSamples(double samples) {
        final double phaseIncrement = this.frequency / this.sampleRate;
        final double newPhase = this.phase + phaseIncrement;
        return createInstance(this.frequency, this.sampleRate, newPhase);
    }

    public DWaveform setFrequency(double frequency) {
        return createInstance(frequency, this.sampleRate, this.phase);
    }

    public DWaveform setSampleRate(double sampleRate) {
        return createInstance(this.frequency, sampleRate, this.phase);
    }

    /**
     * returns the current phase, a phase of 1d is a complete cycle
     */
    public double getPhase() {
        return this.phase;
    }

    /**
     * returns the frequency of the oscillator
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * returns the number of samples per second for the oscillator
     */
    public double getSampleRate() {
        return this.sampleRate;
    }

    /**
     * returns the current amplitude of the wave
     */
    public abstract double getAmplitude();
}
