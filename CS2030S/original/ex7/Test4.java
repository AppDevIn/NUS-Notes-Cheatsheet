import cs2030s.fp.InfiniteList;

/**
 * Test 4 for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList foldRight.
 *
 * @author dcsaysp
 */
class Test4 {
  /**
   * Main method for Test4.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest i = new CS2030STest();

    InfiniteList<Integer> numbers = InfiniteList.iterate(0, x -> x + 1);
    i.expectReturn(
        "InfiniteList.sentinel().foldRight(0, (x, y) -> x + y)",
        () -> InfiniteList.<Integer>sentinel().foldRight(0, (x, y) -> x + y), 0);
    i.expectReturn(
        "InfiniteList.iterate(0, x -> x + 1).limit(5).foldRight(0, (x, y) -> x + y)",
        () -> numbers.limit(5).foldRight(0, (x, y) -> x + y), 10);
    i.expectReturn(
        "InfiniteList.iterate(0, x -> x + 1).limit(0).foldRight(0, (x, y) -> x + y)",
        () -> InfiniteList.iterate(0, x -> x + 1).limit(0).foldRight(0, (x, y) -> x + y), 0);
    i.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * x).limit(5).foldRight(1, (x, y) -> x * y)",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * x).limit(5)
        .foldRight(1, (x, y) -> x * y),
        14400);
    i.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * x).limit(5).foldRight(1, (x, y) -> x - y) evaluates as (1 - (4 - (9 - (16 - (25 - 1)))))",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * x).limit(5)
        .foldRight(1, (x, y) -> x - y),
        (1 - (4 - (9 - (16 - (25 - 1))))));
  }
}