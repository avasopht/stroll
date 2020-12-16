package test;

public abstract class Generator<DGenerator extends Generator> {
    protected double time, amp, sampleRate;
    protected boolean isGateOpened;

    public abstract DGenerator createGenerator(Generator g);

    public abstract double getAmplitude();

    public abstract DGenerator advanceSamples(double samples);

    public void copyParamaters(Generator g) {
        this.time = g.time;
        this.amp = g.amp;
        this.sampleRate = g.sampleRate;
        this.isGateOpened = g.isGateOpened;
    }

    public DGenerator setTime(double time) {
        DGenerator generator = this.createGenerator(this);
        generator.time = time;
        return generator;
    }

    public DGenerator setAmplification(double amp) {
        DGenerator generator = this.createGenerator(this);
        generator.amp = amp;
        return generator;
    }

    public DGenerator setGateOpened(boolean isOpen) {
        DGenerator generator = this.createGenerator(this);
        generator.isGateOpened = isOpen;
        return generator;

    }

    public DGenerator setSampleRate(double sampleRate) {
        DGenerator generator = this.createGenerator(this);
        generator.sampleRate = sampleRate;
        return generator;
    }

    public double getSampleRate() {
        return this.sampleRate;
    }

    public double getTime() {
        return this.time;
    }

    public double getAmplification() {
        return this.amp;
    }

    public boolean isGateOpened() {
        return this.isGateOpened;
    }

}
