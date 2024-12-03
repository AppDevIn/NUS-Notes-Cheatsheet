public class BusStop {
  private int num;
  private String name;

  public BusStop(int num, String name) {
    this.num = num;
    this.name = name;
  }

  public int getNumber() {
    return this.num;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.num + ": " + this.name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof BusStop) {
      BusStop stop = (BusStop) obj;
      return this.num == stop.num
          && this.name.equals(stop.name);
    }
    return false;
  }
}
