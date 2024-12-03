/**
 * This is class C for testing flexibility.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */ 
public class C extends B {
  public C(int x) {
    super(x);
  }

  @Override
  public String toString() {
    return "C:" + this.get();
  }
}