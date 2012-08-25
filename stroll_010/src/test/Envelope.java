package test;

public abstract class Envelope<DEnvelope extends Envelope> {
  protected double level, sampleRate, time;
  protected boolean isGateOpened;

  public abstract double getAmplification();

  public abstract DEnvelope createEnvelope(Envelope e);

  public DEnvelope advanceSamples(double samples) {
    return setTime(this.time + (samples / sampleRate));
  }

  public DEnvelope setGateOpened(boolean isOpen) {
    DEnvelope envelope = createEnvelope(this);
    envelope.isGateOpened = isOpen;
    return envelope;
  }

  public DEnvelope setLevel(double level) {
    DEnvelope envelope = createEnvelope(this);
    envelope.level = level;
    return envelope;
  }

  public DEnvelope setSampleRate(double sampleRate) {
    DEnvelope envelope = createEnvelope(this);
    envelope.sampleRate = sampleRate;
    return envelope;
  }

  public DEnvelope setTime(double time) {
    DEnvelope envelope = createEnvelope(this);
    envelope.time = time;
    return envelope;
  }

  public double getSampleRate() {
    return sampleRate;
  }

  public double getTime() {
    return this.time;
  }

  public double getLevel() {
    return this.level;
  }

  public boolean isGateOpened() {
    return isGateOpened;
  }

}
