/open Perishable.java
/open Named.java
/open Fruit.java
/open FruitStall.java
/open Fruits.jsh

new FruitStall<String>(); // Compilation error

new FruitStall<Fruit>(List.of(
      new Fruit(() -> "Apple", () -> 4),
      new Fruit(() -> "Banana", () -> 1)));

List<Apple> apples = List.of(new Apple(2), new Apple(5));
new FruitStall<Apple>(apples);
new FruitStall<Fruit>(apples);
List<Fruit> list = new FruitStall<Fruit>(apples).getFruits();

List<Fruit> list = List.of(new Mango(), new Apple(5), new Mango());
new FruitStall<>(list).findFruitsByName("Apple");
new FruitStall<>(list).findFruitsByName("Banana");
new FruitStall<>(list).findFruitsByName("Mango");
