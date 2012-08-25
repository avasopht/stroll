package test;

public class Gradient {
  public double m, c;

  public Gradient(double m, double c) {
    this.m = m;
    this.c = c;
  }

  public static Gradient create(double y1, double y2) {
    double m = y2 - y1;
    double c = y1;
    return new Gradient(m, c);
  }

  public double getY(double x) {
    return (m * x) + c;
  }
}
