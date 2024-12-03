// @author A0302149M

public class Fruit {
  private Named namedFunc;
  private Perishable expiryFunc;

  public Fruit(Named namedFunc, Perishable expiryFunc) {
    this.namedFunc = namedFunc;
    this.expiryFunc = expiryFunc;
  }

  public String getName() {
    return this.namedFunc.getName();
  }

  public Integer getDaysToExpiry() {
    return this.expiryFunc.getDaysToExpiry();
  }   

  @Override
  public String toString() {
    return String.format("%s (expires in %s days)", this.getName(), this.getDaysToExpiry());
  }
}
