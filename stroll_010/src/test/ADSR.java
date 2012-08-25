package test;

public class ADSR extends Envelope<ADSR> {
  // ------------------------------------- PROTECTED ----------------------
  protected double attack, decay, sustain, release;

  protected double releaseLevel;

  // -------------------------------------- PUBLIC ------------------------
  public ADSR() {
    this.time = 0d;
    this.sampleRate = 1d;
    this.level = 0d;
    this.isGateOpened = true;

    this.attack = 0d;
    this.decay = 0d;
    this.sustain = 0d;
    this.release = 0d;
    this.releaseLevel = 0d;
  }

  public ADSR(Envelope envelope) {
    this.time = envelope.getTime();
    this.sampleRate = envelope.getSampleRate();
    this.level = envelope.getLevel();
    this.isGateOpened = envelope.isGateOpened;

    if (envelope instanceof ADSR) {
      ADSR adsr = (ADSR) envelope;
      this.attack = adsr.attack;
      this.decay = adsr.decay;
      this.sustain = adsr.sustain;
      this.release = adsr.release;
      this.releaseLevel = adsr.releaseLevel;
    } else {
      this.attack = 0d;
      this.decay = 0d;
      this.sustain = 0d;
      this.release = 0d;
      this.releaseLevel = 0d;
    }
  }

  public ADSR createEnvelope(Envelope e) {
    return new ADSR(e);
  }

  public double getAmplification() {
    double tmp = this.time;

    // return 0 if time is prior to a start
    if (tmp <= 0)
      return 0d;

    // see if we are in attack
    if (tmp < this.attack) {
      if (this.attack <= 0d)
        return 0d;
      else
        return this.level * (tmp / this.attack);
    }
    tmp -= attack;

    // see if we are in decay
    if (tmp < this.decay) {
      if (this.decay <= 0d)
        return 1d;
      return this.level - (this.level * (1d - this.sustain) * tmp / this.decay);
    }
    tmp -= this.decay;

    // stop from releasing if gate is kept open
    if (isGateOpened()) {
      this.time = this.attack + this.decay;
      return this.sustain * this.level;
    }

    // see if we are in release
    if (tmp < this.release) {
      return this.level
          * (this.releaseLevel - (this.releaseLevel * (tmp / this.release)));
    }

    // we are finished
    return 0d;
  }

  public ADSR setGateOpened(boolean isOpened) {
    ADSR adsr = super.setGateOpened(isOpened);

    if (true == isOpened) {
      adsr.releaseLevel = adsr.sustain;
    }

    // if gate is closed and the attack/decay is not complete then
    // change the release (start) level and enter it into release
    if (isOpened == false && adsr.time < adsr.attack + adsr.decay) {
      adsr.releaseLevel = adsr.getAmplification();
      adsr.time = adsr.attack + adsr.decay;
    }
    return adsr;
  }

  public ADSR setAttack(double attack) {
    ADSR envelope = new ADSR(this);
    envelope.attack = attack;
    return envelope;
  }

  public ADSR setDecay(double decay) {
    ADSR envelope = new ADSR(this);
    envelope.decay = decay;
    return envelope;
  }

  public ADSR setSustain(double sustain) {
    ADSR envelope = new ADSR(this);
    envelope.sustain = sustain;
    envelope.releaseLevel = sustain;
    return envelope;
  }

  public ADSR setRelease(double release) {
    ADSR envelope = new ADSR(this);
    envelope.release = release;
    return envelope;
  }

  public ADSR setADSR(double attack, double decay, double sustain,
      double release) {
    ADSR envelope = new ADSR(this);

    envelope.attack = attack;
    envelope.decay = decay;
    envelope.sustain = sustain;
    envelope.release = release;

    envelope.releaseLevel = sustain;

    return envelope;
  }

}
