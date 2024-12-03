/**
 * This is class B for testing flexibility.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */ 
public class B extends A {
  public B(int x) {
    super(x);
  }

  @Override
  public String toString() {
    return "B:" + this.get();
  }
}