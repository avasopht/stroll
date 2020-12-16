package test;

public class GradientGenerator extends Generator<GradientGenerator> {
    protected Waveform wave;
    protected ADSR envelope;

    public Gradient gFrequency, gLevel, gHitRate, gGate;
    public Gradient gAttack, gDecay, gRelease, gSustain;

    public double anticipation;

    public GradientGenerator(Waveform wave) {
        this.wave = wave;
        this.envelope = new ADSR();
        this.amp = 1d;

        anticipation = 0d;

        gFrequency = Gradient.create(1, 1);
        gLevel = Gradient.create(1, 1);
        gHitRate = Gradient.create(1, 1);
        gGate = Gradient.create(1, 1);

        gAttack = Gradient.create(1, 1);
        gDecay = Gradient.create(1, 1);
        gRelease = Gradient.create(1, 1);
        gSustain = Gradient.create(1, 1);
    }

    public GradientGenerator(Generator generator) {
        copyParamaters(generator);
    }

    @Override
    public GradientGenerator setSampleRate(double sampleRate) {
        GradientGenerator generator = super.setSampleRate(sampleRate);
        generator.wave = wave.setSampleRate(sampleRate);
        generator.envelope = envelope.setSampleRate(sampleRate);
        return generator;
    }

    @Override
    public void copyParamaters(Generator g) {
        super.copyParamaters(g);
        if (g instanceof GradientGenerator) {
            GradientGenerator generator = (GradientGenerator) g;
            this.wave = generator.wave;
            this.envelope = generator.envelope;

            this.gFrequency = generator.gFrequency;
            this.gLevel = generator.gLevel;
            this.gHitRate = generator.gHitRate;
            this.gGate = generator.gGate;

            this.gAttack = generator.gAttack;
            this.gDecay = generator.gDecay;
            this.gRelease = generator.gRelease;
            this.gSustain = generator.gSustain;

            this.anticipation = generator.anticipation;
        }
    }

    @Override
    public GradientGenerator setGateOpened(boolean isOpened) {
        GradientGenerator generator = super.setGateOpened(isOpened);
        generator.envelope = generator.envelope.setGateOpened(isOpened);

        if (isOpened == true) {
            generator.wave = generator.wave.setPhase(0d);
            generator.envelope = generator.envelope.setTime(0d);
        }

        return generator;
    }

    @Override
    public GradientGenerator advanceSamples(double samples) {
        double gateTime = gGate.getY(anticipation);

        GradientGenerator generator = createGenerator(this);
        generator.time += samples / sampleRate;

        if (this.time < gateTime & generator.time >= gateTime) {
            generator = generator.setGateOpened(false);
        }

        if (generator.time >= gHitRate.getY(anticipation)) {
            generator.time = 0d;
            generator = generator.setGateOpened(true);
        }

        generator.wave = generator.wave.setFrequency(gFrequency.getY(anticipation));
        generator.wave = generator.wave.advanceSamples(samples);

        generator.envelope = generator.envelope.setAttack(gAttack
                .getY(anticipation));
        generator.envelope = generator.envelope.setDecay(gDecay.getY(anticipation));
        generator.envelope = generator.envelope.setSustain(gSustain
                .getY(anticipation));
        generator.envelope = generator.envelope.setRelease(gRelease
                .getY(anticipation));
        generator.envelope = generator.envelope.setLevel(gLevel.getY(anticipation));
        generator.envelope = generator.envelope.advanceSamples(samples);

        return generator;
    }

    @Override
    public GradientGenerator createGenerator(Generator g) {
        GradientGenerator generator = new GradientGenerator(g);
        generator.copyParamaters(g);
        return generator;
    }

    @Override
    public double getAmplitude() {
        return wave.getAmplitude() * envelope.getAmplification();
    }

}
