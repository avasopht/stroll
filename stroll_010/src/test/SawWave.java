package test;

public class SawWave extends Waveform<SawWave> {
  // ----------------------------- PUBLIC ------------------------
  public SawWave() {
    super();
  }

  private SawWave(double frequency, double sampleRate, double phase) {
    super(frequency, sampleRate, phase);
  }

  public SawWave createInstance(double frequency, double sampleRate,
      double phase) {
    return new SawWave(frequency, sampleRate, phase);
  }

  public double getAmplitude() {
    double amp = (Math.ceil(this.phase) - this.phase);
    return (amp * 2d) - 1d;
  }
}
