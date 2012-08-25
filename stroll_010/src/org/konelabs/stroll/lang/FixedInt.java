package org.konelabs.stroll.lang;

/**
 * <p>
 * Represents a fixed point integer which can interact with another fixed point
 * integer that uses a different fixed point. Like any integer operation you
 * will get precision errors, this class does not attempt to correct these.
 * </p>
 * 
 * <p>
 * Note that if you were to implement such a class where you wanted to create an
 * implementation where precision is better handled then you could create carry
 * flags, etc.
 * </p>
 * 
 * @author konelabs.org
 * 
 */
public final class FixedInt {
  // ======================================== PRIVATE =====================
  private final int exp;
  private final int value;

  // ======================================== PUBLIC =======================
  // ------------------------------------ creation methods ----------------
  /** creates a FixedInt with value=0, exponent=16 */
  public FixedInt() {
    this.value = 0;
    this.exp = 16;
  }

  /** creates a FixedInt copy of the given FixedInt */
  public FixedInt(FixedInt fi) {
    this.value = fi.value;
    this.exp = fi.exp;
  }

  /**
   * creates a FixedInt with the given value and exp <br/>
   * The passed value is the value stored internally, so a value of (1<<16) and
   * an exp of 16 will create a FixedInt equal to 1
   */
  public FixedInt(int value, int exp) {
    this.value = value;
    this.exp = exp;
  }

  /**
   * creates a FixedInt with the given value and exp <br/>
   * The passed value is the value stored internally, so a value of (1<<16) and
   * an exp of 16 will create a FixedInt equal to 1
   */
  public FixedInt(long value, int exp) {
    // without this method passing a long invokes the double constr. badly
    this.value = (int) value;
    this.exp = exp;
  }

  /** creates a FixedInt with the given double with the given exponent */
  public FixedInt(double d, int exp) {
    this.exp = exp;
    this.value = (int) (d * (double) (1 << exp));
  }

  /** creates a FixedInt equal to 0 with the given exponent */
  public FixedInt(int exp) {
    this.value = 0;
    this.exp = exp;
  }

  /** creates a FixedInt equal to d with the same exp as <i>this</i> */
  public FixedInt createDouble(double d) {
    return new FixedInt(d, this.exp);
  }

  /** creates a FixedInt equal to i with the same exp as <i>this</i> */
  public FixedInt createInt(int i) {
    return new FixedInt(i << this.exp, this.exp);
  }

  /** creates a FixedInt equal to i with the given exp */
  public static FixedInt createInt(int i, int exp) {
    return new FixedInt(i << exp, exp);
  }

  // ------------------------------------- getter methods -----------------

  /** returns the internal value of the FixedInt */
  public int getValue() {
    return this.value;
  }

  /** returns the decimal point of the FixedInt */
  public int getExp() {
    return this.exp;
  }

  /** returns the internal value adjusted to correspond with the given exp */
  public int getValue(int newExp) {
    int iValue;

    if (exp < this.exp) {
      // negative right shifts never evaluate to 0 !!!
      if (this.value < 0)
        iValue = -this.value;
      else
        iValue = this.value;

      iValue = iValue >> (this.exp - exp);

      if (this.value < 0)
        iValue = -iValue;
    } else if (newExp > this.exp) {
      iValue = this.value << (newExp - this.exp);
    } else
      iValue = this.value;

    return iValue;
  }

  // ------------------------------------- setter methods -----------------

  /** sets the internal value of the FixedInt irrespective of the exp */
  public FixedInt setValue(long value) {
    return new FixedInt(value, this.exp);
  }

  /** directly sets the decimalPoint of the value */
  public FixedInt setExp(int decimalPoint) {
    return new FixedInt(this.value, decimalPoint);
  }

  /** gives the FixedInt a new exponent and adjusts value accordingly */
  public FixedInt adjustExp(int newExp) {
    int iValue;

    if (newExp < this.exp) {
      // negative right shifts never evaluate to 0 !!!
      if (this.value < 0)
        iValue = -this.value;
      else
        iValue = this.value;

      iValue = iValue >> (this.exp - newExp);

      if (this.value < 0)
        iValue = -iValue;
    } else if (newExp > this.exp) {
      iValue = this.value << (newExp - this.exp);
    } else
      iValue = this.value;

    return new FixedInt(iValue, newExp);

  }

  // ------------------------------------- operation methods ------------

  /**
   * subtracts the given FixedInt from <i>this</i> int
   * 
   * @return: FixedInt with the same exponent as <i>this</i> FixedInt
   */
  public FixedInt sub(FixedInt fi) {
    final int iValue = fi.getValue(this.exp);
    return new FixedInt(this.value - iValue, this.exp);
  }

  /**
   * adds the given FixedInt to <i>this</i> int
   * 
   * @return: FixedInt with the same decimalPoint as <i>this</i> FixedInt
   */
  public FixedInt add(FixedInt fi) {
    final int iValue = fi.getValue(this.exp);
    return new FixedInt(this.value + iValue, this.exp);
  }

  /**
   * multiplies the given FixedInt with <i>this</i> FixedInt
   * 
   * @return: FixedInt with the same exponent as <i>this</i> FixedInt
   */
  public FixedInt mul(FixedInt fi) {
    long newValue = ((long) this.value * (long) fi.value) >> fi.exp;
    return new FixedInt(newValue, this.exp);
  }

  /**
   * divides <i>this</i> FixedInt by the given FixedInt. Does not protect
   * against divide by 0
   * 
   * @return: FixedInt with the same decimalPoint as <i>this</i> FixedInt
   */
  public FixedInt div(FixedInt fi) {
    final long newValue = ((long) this.value << fi.exp) / (long) fi.value;
    return new FixedInt(newValue, this.exp);
  }

  /**
   * amplies <i>this</i> FixedInt by the given FixedInt, such that if fi==0d
   * then the returned value is equal to <i>this</i> FixedInt. If fi==-1d then
   * the returned value is equal to <i>this</i> FixedInt/2d;
   * 
   * @param fi
   * @return amplified <i>FixedInt</i> with the same exp as <i>this</i>
   */
  public FixedInt amp(final FixedInt fi) {
    FixedInt scale = new FixedInt(fi);

    if (scale.getValue() < 0) {
      scale = scale.sub(new FixedInt(1 << 16, 16));
      return div(scale);
    } else {
      scale = scale.add(new FixedInt(1 << 16, 16));
      return mul(scale);
    }
  }

  // ------------------------------------------ test/evaluation methods ---

  /** returns whether the FixedInt is zero */
  public boolean isZero() {
    return this.value == 0;
  }

  /** returns whether this FixedInt is equal to <i>i</i> */
  public boolean equalTo(FixedInt i) {
    // ^_^
    return gte(i) & lte(i);
  }

  /** returns whether this FixedInt is greater than <i>i</i> */
  public boolean gt(FixedInt i) {
    final int iValue = i.getValue(this.exp);
    return this.value > iValue;
  }

  /** returns whether this FixedInt is greater than or equal to <i>i</i> */
  public boolean gte(FixedInt i) {
    final int iValue = i.getValue(this.exp);
    return this.value >= iValue;
  }

  /** returns whether this FixedInt is less than <i>i</i> */
  public boolean lt(FixedInt i) {
    // ^_^
    return !gte(i);
  }

  /** returns whether this FixedInt is less than or equal to <i>i</i> */
  public boolean lte(FixedInt i) {
    // ^_^
    return !gt(i);
  }

  public String toString() {
    return "" + this.toDouble();
  }

  /** returns a double representation of <i>this</i> FixedInt */
  public double toDouble() {
    double dValue = (double) this.value;
    double dExp = (double) (1L << this.exp);
    return dValue / dExp;
  }

  public static void main(String argv[]) {
    FixedInt a, b, c, d, e, f;

    a = new FixedInt(1d, 16);
    b = new FixedInt(5d, 16);
    c = new FixedInt(40d, 16);
    d = a.div(b);
    e = c.mul(d);
    f = a.amp(d);

    System.out.println("Testing multiplication");
    System.out.println("======================");
    System.out.println("a: " + a.toDouble());
    System.out.println("b: " + b.toDouble());
    System.out.println("c: " + c.toDouble());
    System.out.println("d: " + d.toDouble());
    System.out.println("e: " + e.toDouble());
    System.out.println("f: " + f.toDouble());

    a = FixedInt.createInt(20, 16);
    b = FixedInt.createInt(10, 16);
    c = FixedInt.createInt(-5, 16);
    d = a.sub(c);
    e = b.add(b);
    f = c.mul(b);

    System.out.println();
    System.out.println("Testing lt/gt tests");
    System.out.println("-------------------");
    System.out.println("a: " + a.toDouble());
    System.out.println("b: " + b.toDouble());
    System.out.println("c: " + c.toDouble());
    System.out.println("d: " + d.toDouble());
    System.out.println("e: " + e.toDouble());
    System.out.println("f: " + f.toDouble());
    System.out.println("-------------------");
    System.out.println("a < b: " + a.lt(b));
    System.out.println("e==a: " + e.equalTo(a));
  }
}
