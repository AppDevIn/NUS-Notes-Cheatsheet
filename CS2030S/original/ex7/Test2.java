import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test 2 for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList toList.
 *
 * @author dcsaysp
 */
class Test2 {
  /**
   * Main method for Test2.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    List<Integer> incrHistory = new ArrayList<>();
    Transformer<Integer, Integer> incr = x -> {
      incrHistory.add(x);
      return x + 1;
    };

    BooleanCondition<Integer> isEven = x -> (x % 2 == 0);

    we.expectReturn(
        "InfiniteList.<String>sentinel().toList()",
        () -> InfiniteList.<String>sentinel().toList(), List.of());
    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").map(s -> s.length()).limit(2).toList()",
        () -> InfiniteList.iterate("A", s -> s + "Z").map(s -> s.length()).limit(2).toList(),
        List.of(1, 2));
    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").limit(2).map(s -> s.length()).toList()",
        () -> InfiniteList.iterate("A", s -> s + "Z").limit(2).map(s -> s.length()).toList(),
        List.of(1, 2));
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(2).filter(x -> x % 2 == 0).toList()",
        () -> InfiniteList.iterate(1, incr).limit(2).filter(isEven).toList(), List.of(2));
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).limit(2).toList()",
        () -> InfiniteList.iterate(1, incr).filter(isEven).limit(2).toList(), List.of(2, 4));

    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(10).limit(3).toList()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(10).limit(3).toList(), List.of(1, 2, 3));
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(3).limit(10).toList()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(3).limit(10).toList(), List.of(1, 2, 3));

    we.expectReturn(
        "InfiniteList.generate(() -> 4).limit(0).toList()",
        () -> InfiniteList.generate(() -> 4).limit(0).toList(), List.of());
    we.expectReturn(
        "InfiniteList.generate(() -> 4).limit(2).toList()",
        () -> InfiniteList.generate(() -> 4).limit(2).toList(), List.of(4, 4));

    we.expectReturn(
        "InfiniteList.iterate(0, x -> x + 1).filter(x -> x > 10).map(x -> x.hashCode() % 30)" + 
        ".filter(x -> x < 20).limit(5).toList()",
        () -> InfiniteList.iterate(0, x -> x + 1).filter(x -> x > 10).map(x -> x.hashCode() % 30)
        .filter(x -> x < 20).limit(5).toList(),
        List.of(11, 12, 13, 14, 15));

    java.util.Random rng = new java.util.Random(1);
    we.expectReturn(
        "InfiniteList.generate(() -> rng.nextInt() % 100).filter(x -> x > 10).limit(4).toList()",
        () -> InfiniteList.generate(() -> rng.nextInt() % 100).filter(x -> x > 10).limit(4)
        .toList(),
        List.of(76, 95, 26, 69));

    we.expectReturn("InfiniteList.generate(() -> null).limit(4).limit(1).toList()",
        () -> InfiniteList.<Object>generate(() -> null).limit(4).limit(1).toList(), 
        Arrays.asList(new Object[] { null }));
    we.expectReturn("InfiniteList.generate(() -> null).limit(1).limit(4).toList()",
        () -> InfiniteList.<Object>generate(() -> null).limit(1).limit(4).toList(), 
        Arrays.asList(new Object[] { null }));
  }
}