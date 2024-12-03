import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test 1 for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList limit.
 *
 * @author dcsaysp
 */
class Test1 {
  /**
   * Main method for Test1.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.generate(() -> 2).isSentinel()",
        () -> InfiniteList.generate(() -> 2).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.generate(() -> 2).filter(x -> x % 3 == 0).isSentinel()",
        () -> InfiniteList.generate(() -> 2).filter(x -> x % 3 == 0).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).map(x -> 2).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> 2).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).map(x -> 2).isSentinel(), false);

    we.expectReturn("InfiniteList.sentinel().isSentinel()",
        () -> InfiniteList.sentinel().isSentinel(), true);
    we.expectReturn("InfiniteList.sentinel().map(x -> 2).isSentinel()",
        () -> InfiniteList.sentinel().map(x -> 2).isSentinel(), true);
    we.expectReturn("InfiniteList.sentinel().filter(x -> true).isSentinel()",
        () -> InfiniteList.sentinel().filter(x -> true).isSentinel(), true);
    we.expectReturn("InfiniteList.sentinel().filter(x -> false).isSentinel()",
        () -> InfiniteList.sentinel().filter(x -> false).isSentinel(), true);

    we.expectReturn(
        "InfiniteList.sentinel().limit(4).isSentinel()",
        () -> InfiniteList.sentinel().limit(4).isSentinel(), true);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(0).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(0).isSentinel(), true);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(0).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(0).isSentinel(), true);

    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(1).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(1).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(10).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(10).isSentinel(), false);
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(-1).isSentinel()",
        () -> InfiniteList.iterate(1, x -> x + 1).limit(-1).isSentinel(), true);

    List<Integer> incrHistory = new ArrayList<>();
    Transformer<Integer, Integer> incr = x -> {
      incrHistory.add(x);
      return x + 1;
    };
    InfiniteList.iterate(1, incr).limit(0).isSentinel();
    we.expect("InfiniteList.iterate(1, x -> x + 1).limit(0).isSentinel() " + 
        "causes zero evaluation of x -> x + 1", 
        incrHistory, List.of());
    InfiniteList.iterate(1, incr).limit(1).isSentinel();
    we.expect("InfiniteList.iterate(1, x -> x + 1).limit(1).isSentinel() " + 
        "causes zero evaluation of x -> x + 1", 
        incrHistory, List.of());
    InfiniteList.iterate(1, incr).limit(10).isSentinel();
    we.expect("InfiniteList.iterate(1, x -> x + 1).limit(10).isSentinel() " + 
        "causes zero evaluation of x -> x + 1", 
        incrHistory, List.of());

    we.expectReturn("InfiniteList.generate(() -> 1).limit(4).toString()",
        () -> InfiniteList.generate(() -> 1).limit(4).toString(), 
        "[? ?]");
    we.expectReturn("InfiniteList.iterate(1, x -> x + 1).limit(4).toString()",
        () -> InfiniteList.iterate(1, incr).limit(4).toString(), 
        "[[1] ?]");
    we.expectException("InfiniteList.iterate(1, x -> x + 1).limit(0).head()",
        () -> InfiniteList.iterate(1, incr).limit(0).head(), 
        new java.util.NoSuchElementException());
    we.expectReturn("InfiniteList.iterate(1, x -> x + 1).limit(1).head()",
        () -> InfiniteList.iterate(1, incr).limit(1).head(), 1);
    we.expectReturn("InfiniteList.iterate(1, x -> x + 1).limit(4).head()",
        () -> InfiniteList.iterate(1, incr).limit(4).head(), 1);
    we.expectException("InfiniteList.iterate(1, x -> x + 1).limit(1).tail().head()",
        () -> InfiniteList.iterate(1, incr).limit(1).tail().head(), 
        new java.util.NoSuchElementException());
    we.expectReturn("InfiniteList.iterate(1, x -> x + 1).limit(4).tail().tail().head()",
        () -> InfiniteList.iterate(1, incr).limit(4).tail().tail().head(), 3);
    we.expectException("InfiniteList.iterate(1, x -> x + 1).limit(4).limit(1).tail().head()",
        () -> InfiniteList.iterate(1, incr).limit(4).limit(1).tail().head(), 
        new java.util.NoSuchElementException());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).limit(1).limit(4).tail().head()", 
        () -> InfiniteList.iterate(1, incr).limit(1).limit(4).tail().head(), 
        new java.util.NoSuchElementException());

    BooleanCondition<Integer> isEven = x -> (x % 2 == 0);

    we.expectException(
        "InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).limit(0).head()",
        () -> InfiniteList.iterate(1, incr).filter(isEven).limit(0).head(), 
        new java.util.NoSuchElementException());
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).limit(1).head()",
        () -> InfiniteList.iterate(1, incr).filter(isEven).limit(1).head(), 2);
    we.expectException(
        "InfiniteList.iterate(1, x -> x + 1).limit(1).filter(x -> x % 2 == 0).head()",
        () -> InfiniteList.iterate(1, incr).limit(1).filter(isEven).head(), 
        new java.util.NoSuchElementException());
    we.expectReturn(
        "InfiniteList.iterate(1, x -> x + 1).limit(2).filter(x -> x % 2 == 0).head()",
        () -> InfiniteList.iterate(1, incr).limit(2).filter(isEven).head(), 2);

    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").limit(2).map(s -> s.length()).head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").limit(2).map(s -> s.length()).head(), 1);
    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").limit(2).map(s -> s.length()).tail().head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").limit(2).map(s -> s.length())
        .tail().head(), 2);
    we.expectException(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").limit(2).map(s -> s.length()).tail().tail()" +
        ".head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").limit(2).map(s -> s.length()).tail().tail()
        .head(),
        new java.util.NoSuchElementException());

    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").map(s -> s.length()).limit(2).head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").map(s -> s.length()).head(), 1);
    we.expectReturn(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").map(s -> s.length()).limit(2).tail().head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").map(s -> s.length()).tail().head(), 2);
    we.expectException(
        "InfiniteList.iterate(\"A\", s -> s + \"Z\").map(s -> s.length()).limit(2).tail().tail()" +
        ".head()",
        () -> InfiniteList.iterate("A", s -> s + "Z").map(s -> s.length()).limit(2).tail().tail()
        .head(), 
        new java.util.NoSuchElementException());
  }
}