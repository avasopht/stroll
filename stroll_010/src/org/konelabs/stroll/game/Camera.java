package org.konelabs.stroll.game;

import org.konelabs.stroll.graphics.GBAGraphics;
import org.konelabs.stroll.graphics.GBALayer;
import org.konelabs.stroll.graphics.InterruptListener;

import test.Gradient;
import test.GradientGenerator;
import test.SineWave;

/**
 * LayerEffects applies effects to a layer
 * 
 * @author Keldon
 * 
 */
public class Camera implements InterruptListener {
  // ---------------------- PUBLIC VARIABLES

  /** bitmap origin, i.e. centre of bitmap (16:16) */
  public int ox = 0, oy = 0;
  /** angle of bitmap (360 degrees = 1024) */
  public int angle = 0;
  /** tile factor 0-1 */
  public double tilt = 0;
  /** scale of bitmap (16:16) */
  public int scale = 1 << 16;
  /** bitmap translation, i.e. translation away from it's natural centre (16:16) */
  public int tx = 0, ty = 0;

  // TODO: introduce variable to decide whether origin refers to centre of
  // screen or corner

  // ------------------------ PRIVATE VARIABLES
  private GBALayer bgLayer = null;
  // sine wave sequencer variables

  private GradientGenerator generator, tiltGen, rotGen;

  // ---------------------------- PRIVATE METHODS -

  private void nextFrame() {

    // range: -256 -- 256 (.16)
    // int zoomLevel = sin(phase) * (amp-1);
    int zoomLevel = (int) ((1 << 16) * generator.getAmplitude());

    if (zoomLevel < 0)
      zoomLevel -= 1 << 16;
    else if (zoomLevel > 0)
      zoomLevel += 1 << 16;

    if (zoomLevel == 0) {
      scale = 1 << 16;
    } else if (zoomLevel > 0) {
      scale = zoomLevel;
    } else if (zoomLevel < 0) {
      scale = (int) ((long) (1L << 32) / (long) -zoomLevel);
    }

    generator = generator.advanceSamples(1d);
    tiltGen = tiltGen.advanceSamples(1d);
    rotGen = rotGen.advanceSamples(1d);

  }

  /**
   * 
   * @param alpha
   *          : angle range = 0-1023
   * @return 16:16 sine result
   */
  private static int sin(int alpha) {
    double radians = (double) (alpha) * (2 * Math.PI) / 256d;
    return (int) (Math.sin(radians) * (double) (1 << 16));
  }

  private static int cos(int alpha) {
    double radians = (double) (alpha) * (2 * Math.PI) / 256d;
    return (int) (Math.cos(radians) * (double) (1 << 16));
  }

  // ---------------------- PUBLIC CONSTRUCTOR
  public Camera() {
    generator = new GradientGenerator(new SineWave());
    generator = generator.setSampleRate(60d);
    generator.gFrequency = Gradient.create(0.9d, 4d);
    generator.gLevel = Gradient.create(0.5, 0.25);
    generator.gAttack = Gradient.create(0.1, 0.1);
    generator.gDecay = Gradient.create(0.1, 0.05);
    generator.gSustain = Gradient.create(0.3, 0.2);
    generator.gRelease = Gradient.create(5, 3);
    generator.gHitRate = Gradient.create(15, 10);
    generator.gGate = Gradient.create(0.2, 0.2);
    generator.anticipation = 0.2d;

    tiltGen = new GradientGenerator(generator);
    rotGen = new GradientGenerator(tiltGen);
    tiltGen.anticipation = 1d;
  }

  // ------------------------- PUBLIC METHODS
  /** assigns the given bgLayer */
  public void setBGLayer(GBALayer bgLayer) {
    this.bgLayer = bgLayer;
    GBAGraphics.getInstance().setHBLListener(this);
    GBAGraphics.getInstance().setHBLEnable(true);
  }

  /** applies layer effects to BGLayer */
  public void applyEffects() {
    int ctrx = GBAGraphics.getInstance().getWidth() / 2;
    int ctry = GBAGraphics.getInstance().getHeight() / 2;
    int A, B, C, D, xRef, yRef;
    // rotation centre
    int x0, y0;
    // position of top left pixel before rot/scaling
    int x1, y1;

    if (scale == 0)
      return;

    // create screen setup
    A = (int) (((long) (cos(angle)) << 16) / (long) scale); // distance moved in
                                                            // direction x, same
                                                            // line
    B = (int) (((long) (-sin(angle)) << 16) / (long) scale); // distance moved
                                                             // in direction x,
                                                             // next line
    C = (int) (((long) (sin(angle)) << 16) / (long) scale); // distance moved in
                                                            // direction y, same
                                                            // line
    D = (int) (((long) (cos(angle)) << 16) / (long) scale); // distance moved in
                                                            // direction y, next
                                                            // line

    // calculate texture reference
    x0 = ox;
    y0 = oy;
    x1 = -ctrx + (ox - tx);
    y1 = -ctry + (oy - ty);

    xRef = A * (x1 - x0) + B * (y1 - y0) + (x0 << 16);
    yRef = C * (x1 - x0) + D * (y1 - y0) + (y0 << 16);

    // save calculations to BGLayer
    bgLayer.PA = A;
    bgLayer.PB = B;
    bgLayer.PC = C;
    bgLayer.PD = D;
    bgLayer.xRef = xRef;
    bgLayer.yRef = yRef;

    // prepare for next frame
    nextFrame();
  }

  final public void screenInterrupt(int interruptType) {
    if (interruptType != InterruptListener.HBL_INTERRUPT)
      return;

    // centre x, centre y
    int ctrx = GBAGraphics.getInstance().getWidth() / 2;
    int ctry = GBAGraphics.getInstance().getHeight() / 2;
    int curScale;
    int A, B, C, D, xRef, yRef;

    int yDist = GBAGraphics.getInstance().getVCount() - ctry;
    double dScale = (double) (tiltGen.getAmplitude() * yDist) / (double) ctry;

    curScale = scale + (int) (scale * dScale);

    // rotation centre
    int x0, y0;
    // position of top left pixel before rot/scaling
    int x1, y1;

    if (curScale == 0)
      return;

    // create screen setup
    A = (int) (((long) (cos(angle)) << 16) / (long) curScale); // distance moved
                                                               // in direction
                                                               // x, same line
    B = (int) (((long) (-sin(angle)) << 16) / (long) curScale); // distance
                                                                // moved in
                                                                // direction x,
                                                                // next line
    C = (int) (((long) (sin(angle)) << 16) / (long) curScale); // distance moved
                                                               // in direction
                                                               // y, same line
    D = (int) (((long) (cos(angle)) << 16) / (long) curScale); // distance moved
                                                               // in direction
                                                               // y, next line

    // calculate texture reference
    x0 = ox;
    y0 = oy;
    x1 = -ctrx + (ox - tx);
    y1 = -ctry + (oy - ty);

    xRef = A * (x1 - x0) + B * (y1 - y0) + (x0 << 16);
    yRef = C * (x1 - x0) + D * (y1 - y0) + (y0 << 16);

    // save calculations to BGLayer
    bgLayer.PA = A;
    bgLayer.PB = B;
    bgLayer.PC = C;
    bgLayer.PD = D;
    bgLayer.xRef = xRef;
    bgLayer.yRef = yRef;

  }
}
