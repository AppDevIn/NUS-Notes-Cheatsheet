import java.util.List;

class Test2 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectCompile("Testing that FruitStall has a bounded type parameter", 
        "new FruitStall<String>();",
        false);

    List<Fruit> list = List.of(new Fruit(() -> "Apple", () -> 4), 
        new Fruit(() -> "Banana", () -> 1));

    we.expect("Create a new FruitStall<Fruit> with a list of fruits",
        new FruitStall<Fruit>(list).toString().trim(),
        "- Apple (expires in 4 days)\n- Banana (expires in 1 days)");

    class Apple extends Fruit {
      public Apple(int i) {
        super(() -> "Apple", () -> i);
      }
    }

    List<Apple> apples = List.of(new Apple(2), new Apple(5));

    we.expect("Create a new FruitStall<Apple> with a list of apples",
        new FruitStall<Apple>(apples).toString().trim(),
        "- Apple (expires in 2 days)\n- Apple (expires in 5 days)");

    we.expect("Create a new FruitStall<Fruit> with a list of apples",
        new FruitStall<Fruit>(apples).toString().trim(),
        "- Apple (expires in 2 days)\n- Apple (expires in 5 days)");

    we.expect("Testing FruitStall::getFruits",
        new FruitStall<Fruit>(apples).getFruits().toString().trim(),
        "[Apple (expires in 2 days), Apple (expires in 5 days)]");

    class Melon extends Fruit {
      public Melon(int i) {
        super(() -> "Melon", () -> i);
      }
    }

    List<Fruit> list2 = List.of(new Melon(10), new Apple(4), new Melon(17));
    we.expect("FruitStall with two melons, one apple, and zero mangoes.\n" 
        + "..Testing FruitStall::findFruitsByName with Apple",
        new FruitStall<>(list2).findFruitsByName("Apple").toString().trim(),
        "[Apple (expires in 4 days)]");

    we.expect("..Testing FruitStall::findFruitsByName with Melon",
        new FruitStall<>(list2).findFruitsByName("Melon").toString().trim(),
        "[Melon (expires in 10 days), Melon (expires in 17 days)]");

    we.expect("..Testing FruitStall::findFruitsByName with Mango",
        new FruitStall<>(list2).findFruitsByName("Mango").toString().trim(),
        "[]");

  }
}
