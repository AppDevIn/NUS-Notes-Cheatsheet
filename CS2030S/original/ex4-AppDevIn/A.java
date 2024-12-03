/**
 * This is class A for testing flexibility.
 *
 * @author dcsaysp
 * @version CS2030S AY24/25 Semester 1
 */ 
public class A {
  private int x;
  
  public A(int x) {
    this.x = x;
  }

  public int get() {
    return this.x;
  }

  @Override
  public String toString() {
    return "A:" + this.get();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof A a) {
      return this.x == a.x;
    }
    return false;
  }
}