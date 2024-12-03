import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Producer;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * Test 0 Part B for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList map().
 *
 * @author dcsaysp
 */
class Test0B {
    /**
     * Main method for Test0B.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest we = new CS2030STest();
    
    we.expectReturn(
        "InfiniteList.generate(() -> 1).map(x -> x * 2).toString()",
        () -> InfiniteList.generate(() -> 1).map(x -> x * 2).toString(), 
        "[? ?]");
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).toString()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).toString(), 
        "[? ?]");

    we.expectReturn(
        "InfiniteList.generate(() -> 1).map(x -> x * 2).head()",
        () -> InfiniteList.generate(() -> 1).map(x -> x * 2).head(), 2);
    we.expectReturn(
        "InfiniteList.generate(() -> 1).map(x -> x * 2).tail().head()",
        () -> InfiniteList.generate(() -> 1).map(x -> x * 2).tail().head(), 2);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).head()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).head(), 2);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).tail().head()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).tail().head(), 4);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).map(x -> x - 1).head()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).map(x -> x - 1).head(), 1);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).map(x -> x - 1).tail().head()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x * 2).map(x -> x - 1).tail().head(), 3);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> x % 2 == 0 ? null : x).tail().head()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> x % 2 == 0 ? null : x).tail().head(),
        null);

    List<Integer> doublerHistory = new ArrayList<>();
    List<Integer> generateHistory = new ArrayList<>();
    Producer<Integer> generator = () -> { 
      generateHistory.add(1); 
      return 1; 
    };
    Transformer<Integer, Integer> doubler = x -> { 
      doublerHistory.add(x); 
      return x * 2; 
    };
    // Transformer<Integer,Integer> oneLess = x -> { doublerHistory.add(x); return x - 1; };

    we.expect("InfiniteList.generate(() -> 1).map(x -> x * 2).tail().head()\n" +
        " ..returns 2",
        () -> InfiniteList.generate(generator).map(doubler).tail().head(), 2);
    we.expect(" ..causes two evals of () -> 1",
        generateHistory, List.of(1, 1));
    we.expect(" ..causes two evals of x -> x * 2",
        doublerHistory, List.of(1, 1));

    generateHistory.retainAll(List.of());
    doublerHistory.retainAll(List.of());
    InfiniteList<Integer> ones = InfiniteList.generate(generator);
    InfiniteList<Integer> twos = ones.map(doubler);
    twos.tail().head();
    we.expect("InfiniteList<Integer> ones = InfiniteList.generate(() -> 1)\n" + 
        "InfiniteList<Integer> twos = ones.map(x -> x * 2)\n" + 
        "After twos.tail().head()\n" +
        " ..ones.toString() returns [[1] [[1] ?]]",
        () -> ones.toString(), "[[1] [[1] ?]]");
    we.expect(" ..twos.toString() returns [[2] [[2] ?]]",
        () -> twos.toString(), "[[2] [[2] ?]]");
    twos.head();
    we.expect(" ..calling twos.head() again\n" + 
        " ....causes zero evaluation of () -> 1",
        generateHistory, List.of(1, 1));
    we.expect(" ....causes zero evaluation of x -> x * 2",
        doublerHistory, List.of(1, 1));
    we.expect(" ..calling twos.tail().head() again\n" +
        " ....causes zero evaluation of () -> 1",
        generateHistory, List.of(1, 1));
    we.expect(" ....causes zero evaluation of x -> x * 2",
        doublerHistory, List.of(1, 1));

  }
}