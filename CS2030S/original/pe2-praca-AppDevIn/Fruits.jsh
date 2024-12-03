class Apple extends Fruit {
  public Apple(int i) {
    super(() -> "Apple", () -> i);
  }
}

class Mango extends Fruit {
  public Mango() {
    super(() -> "Mango", () -> 4);
  }
}

class Devil extends Fruit {
  Devil() {
    super(() -> "Devil", () -> 100000);
  }
}
