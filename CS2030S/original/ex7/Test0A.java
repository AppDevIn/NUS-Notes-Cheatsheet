import cs2030s.fp.InfiniteList;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * Test 0 Part A for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList filter().
 *
 * @author dcsaysp
 */
class Test0A {
    /**
     * Main method for Test0A.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {

    CS2030STest we = new CS2030STest();

    we.expectReturn(
        "InfiniteList.generate(() -> 1).toString()",
        () -> InfiniteList.generate(() -> 1).toString(), "[? ?]");

    we.expectReturn(
        "InfiniteList.generate(() -> 1).head()",
        () -> InfiniteList.generate(() -> 1).head(), 1);

    we.expectReturn(
        "InfiniteList.generate(() -> null).tail().head()",
        () -> InfiniteList.generate(() -> null).tail().head(), null);

    we.expectReturn(
        "InfiniteList.iterate(\"A\", x -> x + \"Z\").head()",
        () -> InfiniteList.iterate("A", x -> x + "Z").head(), "A");

    we.expectReturn(
        "InfiniteList.iterate(\"A\", x -> x + \"Z\").tail().head()",
        () -> InfiniteList.iterate("A", x -> x + "Z").tail().head(), "AZ");

    we.expectReturn(
        "InfiniteList.iterate(\"A\", x -> x + \"Z\").tail().tail().head()",
        () -> InfiniteList.iterate("A", x -> x + "Z").tail().tail().head(), "AZZ");

    List<Integer> evalHistory = new ArrayList<>();
    Transformer<Integer, Integer> op = x -> { 
      evalHistory.add(x); 
      return x + 1; 
    };

    InfiniteList<Integer> numbers = InfiniteList.iterate(1, op);
    numbers.head();
    we.expectReturn(
        "InfiniteList<Integer> numbers = InfiniteList.iterate(1, x -> x + 1);\n" +
        "numbers.toString()",
        () -> numbers.toString(), "[[1] ?]");
    we.expect("numbers.head() causes zero evaluation of x -> x + 1)",
        evalHistory, List.of());
    we.expectReturn("numbers.toString())",
        () -> numbers.toString(), "[[1] ?]");
    numbers.tail().head();
    we.expect("numbers.tail().head() causes one evaluation of x -> x + 1",
        evalHistory, List.of(1));
    we.expectReturn("numbers.toString())",
        () -> numbers.toString(), "[[1] [[2] ?]]");
    numbers.tail().head();
    we.expect("numbers.tail().head() (again) causes zero evaluation of x -> x + 1",
        evalHistory, List.of(1));
    we.expectReturn("numbers.toString())",
        () -> numbers.toString(), "[[1] [[2] ?]]");
    numbers.tail().tail().head();
    we.expect("numbers.tail().tail().head() causes one evaluation of x -> x + 1",
        evalHistory, List.of(1, 2));
    we.expectReturn("numbers.toString())",
        () -> numbers.toString(), "[[1] [[2] [[3] ?]]]");
    numbers.tail().head();
    we.expect("numbers.tail().head() (again) causes zero evaluation of x -> x + 1",
        evalHistory, List.of(1, 2));
    we.expectReturn("numbers.toString())",
        () -> numbers.toString(), "[[1] [[2] [[3] ?]]]");

    InfiniteList<Integer> zeros = InfiniteList.generate(() -> { 
      evalHistory.add(0); 
      return 0; 
    });
    evalHistory.retainAll(List.of());
    we.expect("InfiniteList<Integer> zeros = InfiniteList.generate(() -> 0)\n" +
        "zeros.toString() returns [? ?]",
        () -> zeros.toString(), "[? ?]");

    zeros.head();
    we.expect("zeros.head() causes one evaluation of () -> 0",
        evalHistory, List.of(0));
    we.expect("zeros.toString() returns [[0] ?]",
        () -> zeros.toString(), "[[0] ?]");

    zeros.tail().head();
    we.expect("zeros.tail().head() causes one evaluation of () -> 0)",
        evalHistory, List.of(0, 0));
    we.expect("zeros.toString() returns [[0] [[0 ?]]]",
        () -> zeros.toString(), "[[0] [[0] ?]]");

    zeros.head();
    we.expect("zeros.head() (again) causes zero evaluation of () -> 0",
        evalHistory, List.of(0, 0));
    we.expect("zeros.toString() returns [[0] [[0] ?]]",
        () -> zeros.toString(), "[[0] [[0] ?]]");

    zeros.tail().head();
    we.expect("zeros.tail().head() causes zero evaluation of () -> 0",
        evalHistory, List.of(0, 0));
    we.expect("zeros.toString() returns [[0] [[0] ?]]",
        () -> zeros.toString(), "[[0] [[0] ?]]");
        
    zeros.tail().tail().head();
    we.expect("zeros.tail().tail().head() causes one more evaluation of () -> 0",
        evalHistory, List.of(0, 0, 0));
    we.expect("zeros.toString() returns [[0] [[0] [[0] ?]]]",
        () -> zeros.toString(), "[[0] [[0] [[0] ?]]]");

    zeros.tail().head();
    we.expect("zeros.tail().head() (again) causes zero evaluation of () -> 0",
        evalHistory, List.of(0, 0, 0));
    we.expect("zeros.toString() returns [[0] [[0] [[0] ?]]]",
        () -> zeros.toString(), "[[0] [[0] [[0] ?]]]");
  }
}