import java.util.Arrays;
import java.util.List;

class Test3 {
  public static void main(String[] args) {

    class Apple extends Fruit {
      public Apple(int i) {
        super(() -> "Apple", () -> i);
      }
    }

    class Devil extends Fruit {
      Devil() {
        super(() -> "Devil", () -> 100000);
      }
    }

    class Mango extends Fruit {
      Mango() {
        super(() -> "Mango", () -> 4);
      }
    }

    FruitStall<Apple> fs1 = new FruitStall<>(List.of(new Apple(1), new Apple(9)));
    FruitStall<Fruit> fs2 = new FruitStall<>(List.of(new Apple(2)));

    CS2030STest we = new CS2030STest();

    we.expect("Q4: Merging stalls of the same fruit type",
        () -> Q4.mergeStalls(fs1, fs2).toString().trim(),
        "- Apple (expires in 1 days)\n- Apple (expires in 9 days)\n"
        + "- Apple (expires in 2 days)");

    we.expect("Q4: Merging stalls with an empty stall",
        () -> Q4.mergeStalls(fs1, new FruitStall<>()).toString().trim(),
        "- Apple (expires in 1 days)\n- Apple (expires in 9 days)");

    we.expectCompile("Q4: Merging stalls of different fruits into a fruit stall", 
        "class A extends Fruit { A() { super(() -> \"A\", () -> 1); } }" +
        "class B extends Fruit { B() { super(() -> \"B\", () -> 1); } }" +
        "FruitStall<A> a = new FruitStall<>();" + 
        "FruitStall<B> b = new FruitStall<>();" + 
        "FruitStall<Fruit> fs = Q4.mergeStalls(a, b);", 
        true);

    we.expectCompile("Q4: Merging stalls of different fruits into an incompatible stall", 
        "class A extends Fruit { A() { super(() -> \"A\", () -> 1); } }" +
        "class B extends Fruit { B() { super(() -> \"B\", () -> 1); } }" +
        "FruitStall<A> a = new FruitStall<>();" + 
        "FruitStall<B> b = new FruitStall<>();" + 
        "FruitStall<A> fs = Q4.mergeStalls(a, b);", 
        false);

    FruitStall<Fruit> fs3 = new FruitStall<>(List.of(new Apple(8), new Mango()));
    FruitStall<Mango> fs4 = new FruitStall<>(List.of(new Mango()));
    we.expect("Q5: Testing Q5::findUniqueFruitTypes",
        () -> Q5.findUniqueFruitTypes(List.of(fs1, fs2, fs3, fs4)).toString().trim(),
        "[Apple, Mango]");

    we.expect("Q5: Testing Q5::findUniqueFruitTypes",
        () -> Q5.findUniqueFruitTypes(List.of(fs4, fs3, fs2, fs1)).toString().trim(),
        "[Apple, Mango]");

    we.expect("Q5: Testing Q5::findUniqueFruitTypes with only one fruit type",
        () -> Q5.findUniqueFruitTypes(List.of(fs4)).toString().trim(),
        "[Mango]");

    FruitStall<Devil> fs5 = new FruitStall<>(List.of(new Devil(), new Devil()));

    we.expect("Q6: Testing Q6::consolidateStallsbyType",
        () -> Q6.consolidateStallsbyType(List.of(fs3, fs4, fs5)).toString().trim(),
        "[\n- Apple (expires in 8 days)"
        + "\n, "
        + "\n- Devil (expires in 100000 days)"
        + "\n- Devil (expires in 100000 days)"
        + "\n, "
        + "\n- Mango (expires in 4 days)"
        + "\n- Mango (expires in 4 days)"
        + "\n]");

    we.expect("Q7: Comparing two equal fruit stalls",
        () -> new Q7<>().compare(fs3, fs4) == 0,
        true);

    we.expect("Q7: Comparing two fruit stalls: first fruit stall less than the second",
        () -> new Q7<>().compare(fs4, fs5) < 0,
        true);

    we.expect("Q7: Comparing two fruit stalls: first fruit stall larger than the second",
        () -> new Q7<>().compare(fs5, fs4) > 0,
        true);

    we.expect("Q8: Testing Q8::sortByShortestExpiry method",
        () -> Q8.sortByShortestExpiry(List.of(fs1, fs2, fs3, fs4, fs5)).toString(),
        "[\n"
        + "- Apple (expires in 1 days)\n"
        + "- Apple (expires in 9 days)\n"
        + ", \n"
        + "- Apple (expires in 2 days)\n"
        + ", \n"
        + "- Apple (expires in 8 days)\n"
        + "- Mango (expires in 4 days)\n"
        + ", \n"
        + "- Mango (expires in 4 days)\n"
        + ", \n"
        + "- Devil (expires in 100000 days)\n"
        + "- Devil (expires in 100000 days)\n"
        + "]");
  }
}
