import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Producer;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * Test 0 Part C for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList filter().
 *
 * @author dcsaysp
 */
class Test0C {
    /**
     * Main method for Test0C.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest we = new CS2030STest();

    we.expectReturn("InfiniteList.generate(() -> 1).filter(x -> x % 2 == 0).toString()",
        () -> InfiniteList.generate(() -> 1).filter(x -> x % 2 == 0).toString(),
        "[? ?]");
    we.expectReturn("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).toString()",
        () -> InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).toString(),
        "[? ?]");

    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).head() returns 2",
        InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).head(), 2);
    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0)" +
        ".filter(x -> x > 4).head() returns 6",
        InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).filter(x -> x > 4).head(), 6);

    List<Integer> incrHistory = new ArrayList<>();
    List<Integer> isEvenHistory = new ArrayList<>();
    BooleanCondition<Integer> isEven = x -> { 
      isEvenHistory.add(x); 
      return x % 2 == 0; 
    };
    Transformer<Integer, Integer> incr = x -> { 
      incrHistory.add(x); 
      return x + 1; 
    };

    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).tail() " + 
        ".head()\n" + " ..returns 4", 
        InfiniteList.iterate(1, incr).filter(isEven).tail().head(), 4);
    we.expect(" ..causes three evals on x -> x + 1", 
        incrHistory, List.of(1, 2, 3));
    we.expect(" ..causes four evals on x -> x % 2 == 0", 
        isEvenHistory, List.of(1, 2, 3, 4));

    incrHistory.retainAll(List.of());
    isEvenHistory.retainAll(List.of());

    InfiniteList<Integer> nums = InfiniteList.iterate(1, incr);
    InfiniteList<Integer> evens = nums.filter(isEven);
    evens.tail().head();
    we.expect("InfiniteList<Integer> nums = InfiniteList.iterate(1, x -> x + 1)\n" + 
        "InfiniteList<Integer> evens = nums.filter(x -> x % 2 == 0)\n" + 
        "After evens.tail().head()\n" +
        " ..nums.toString() returns [[1] [[2] [[3] [[4] ?]]]]",
        nums.toString(), "[[1] [[2] [[3] [[4] ?]]]]");
    we.expect(" ..evens.toString() returns [[] [[2] [[] [[4] ?]]]]",
        evens.toString(), "[[] [[2] [[] [[4] ?]]]]");
    nums.tail().head();
    we.expect(" ..calling nums.tail().head()\n" + 
        " ....causes zero evaluation of x -> x + 1", 
        incrHistory, List.of(1, 2, 3));
    evens.tail().head();
    we.expect(" ..calling evens.tail().head()\n" + 
        " ....causes zero evaluation of x -> x % 2 == 0", 
        isEvenHistory, List.of(1, 2, 3, 4));
    we.expect(" ....causes zero evaluation of x -> x + 1", 
        incrHistory, List.of(1, 2, 3));

    incrHistory.retainAll(List.of());
    isEvenHistory.retainAll(List.of());

    List<Integer> moreThan5History = new ArrayList<>();
    BooleanCondition<Integer> moreThan5 = x -> { 
      moreThan5History.add(x); 
      return x > 5; 
    };

    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x > 5).filter(x -> x % 2 == 0)" +
        ".head()\n" +
        " ..returns 6",
        InfiniteList.iterate(1, incr).filter(moreThan5).filter(isEven).head(), 6);
    we.expect(" ..causes five evals on x -> x + 1",
        incrHistory, List.of(1, 2, 3, 4, 5));
    we.expect(" ..causes six evals on x -> x > 5",
        moreThan5History, List.of(1, 2, 3, 4, 5, 6));
    we.expect(" ..causes one evals on x -> x % 2 == 0",
        isEvenHistory, List.of(6));

    incrHistory.retainAll(List.of());
    isEvenHistory.retainAll(List.of());
    moreThan5History.retainAll(List.of());

    List<Integer> doublerHistory = new ArrayList<>();
    Transformer<Integer, Integer> doubler = x -> { 
      doublerHistory.add(x); 
      return x * 2; 
    };

    we.expect("InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).filter(x -> x > 5)" +
        ".filter(x -> x % 2 == 0).tail().head()\n" +
        " ..returns 6",
        InfiniteList.iterate(1, incr).map(doubler).filter(moreThan5).filter(isEven)
        .tail().head(), 8);
    we.expect(" ..causes three evals on x -> x + 1",
        incrHistory, List.of(1, 2, 3));
    we.expect(" ..causes four evals on x -> x * 2",
        doublerHistory, List.of(1, 2, 3, 4));
    we.expect(" ..causes four evals on x -> x > 5",
        moreThan5History, List.of(2, 4, 6, 8));
    we.expect(" ..causes two evals on x -> x % 2 == 0",
        isEvenHistory, List.of(6, 8));

    doublerHistory.retainAll(List.of());
    incrHistory.retainAll(List.of());
    isEvenHistory.retainAll(List.of());
    moreThan5History.retainAll(List.of());

    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).map(x -> x * 2)" +
        ".filter(x -> x > 5).head()\n" +
        " ..returns 8",
        InfiniteList.iterate(1, incr).filter(isEven).map(doubler).filter(moreThan5).head(), 8);
    we.expect(" ..causes three evals on x -> x + 1",
        incrHistory, List.of(1, 2, 3));
    we.expect(" ..causes four evals on x -> x % 2 == 0",
        isEvenHistory, List.of(1, 2, 3, 4));
    we.expect(" ..causes two evals on x -> x * 2",
        doublerHistory, List.of(2, 4));
    we.expect(" ..causes two evals on x -> x > 5",
        moreThan5History, List.of(4, 8));
  }
}