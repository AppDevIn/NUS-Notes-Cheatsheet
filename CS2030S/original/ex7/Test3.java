import cs2030s.fp.BooleanCondition;
import cs2030s.fp.InfiniteList;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

/**
 * Test 3 for CS2030S Lab 7 (AY24/25 Sem 1).  Tests
 * for InfiniteList takeWhile.
 *
 * @author dcsaysp
 */
class Test3 {
  /**
   * Main method for Test3.
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

    List<Integer> lessThan0History = new ArrayList<>();
    BooleanCondition<Integer> lessThan0 = x -> { 
      lessThan0History.add(x);
      return x < 0;
    };

    we.expect("InfiniteList.<Integer>sentinel().takeWhile(x -> x < 0).isSentinel()\n" +
        " ..returns true",
        () -> InfiniteList.<Integer>sentinel().takeWhile(x -> x < 0).isSentinel(), true);

    we.expect("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 0).isSentinel()\n" +
        " ..returns false",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan0).isSentinel(), false);
    we.expect(" ..causes zero evaluation of x -> x + 1",
        incrHistory, List.of());
    we.expect(" ..causes zero evaluation of x -> x < 0",
        lessThan0History, List.of());

    List<Integer> lessThan2History = new ArrayList<>();
    BooleanCondition<Integer> lessThan2 = x -> { 
      lessThan2History.add(x);
      return x < 2;
    };

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    we.expect("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 2).isSentinel()\n" +
        " ..returns false",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan2).isSentinel(), false);
    we.expect(" ..causes zero evaluation of x -> x + 1",
        incrHistory, List.of());
    we.expect(" ..causes zero evaluation of x -> x < 2",
        lessThan2History, List.of());

    we.expect("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 5).takeWhile(x -> x < 3)" +
        ".toList()\n" + " ..returns [1, 2]",
        () -> InfiniteList.iterate(1, incr).takeWhile(x -> x < 5).takeWhile(x -> x < 3).toList(), 
        List.of(1, 2));
    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0).takeWhile(x -> x < 10)" +
        ".toList()\n " + " ..returns [2, 4, 6, 8]",
        () -> InfiniteList.iterate(1, incr).filter(x -> x % 2 == 0).takeWhile(x -> x < 10).toList(),
        List.of(2, 4, 6, 8));

    we.expectReturn("() -> InfiniteList.generate(() -> 2).takeWhile(lessThan0).toString()",
        () -> InfiniteList.generate(() -> 2).takeWhile(lessThan0).toString(),
        "[? ?]");
    we.expectReturn("() -> InfiniteList.iterate(1, incr).takeWhile(lessThan0).toString()",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan0).toString(),
        "[? ?]");
    incrHistory.retainAll(List.of());
    lessThan0History.retainAll(List.of());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 0).head()\n" + 
        " ..throws exception",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan0).head(), 
        new java.util.NoSuchElementException());
    we.expect(" ..causes zero evaluation of x -> x + 1",
        incrHistory, List.of());
    we.expect(" ..causes one evaluation of x -> x < 0",
        lessThan0History, List.of(1));

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    we.expect("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 2).head()\n" +
        " ..returns 1",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan2).head(), 1);
    we.expect("..causes zero evaluation of x -> x + 1",
        incrHistory, List.of());
    we.expect("..causes one evaluation of x -> x < 2",
        lessThan2History, List.of(1));

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 2).tail().head()\n" +
        " ..throws exception",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan2).tail().head(), 
        new java.util.NoSuchElementException());
    we.expect(" ..causes one evaluation of x -> x + 1",
        incrHistory, List.of(1)); 
    we.expect(" ..causes two evaluation of x -> x < 2",
        lessThan2History, List.of(1, 2)); 

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    lessThan0History.retainAll(List.of());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 2)" + 
        ".takeWhile(x -> x < 0).head()\n" +
        " ..throws exception", 
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan2).takeWhile(lessThan0).head(), 
        new java.util.NoSuchElementException());
    we.expect(" ..causes zero evaluation of x -> x + 1",
        incrHistory, List.of()); 
    we.expect(" ..causes one evaluation of x -> x < 2",
        lessThan2History, List.of(1)); 
    we.expect(" ..causes one evaluation of x -> x < 0",
        lessThan0History, List.of(1)); 

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    lessThan0History.retainAll(List.of());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 0)" + 
        ".takeWhile(x -> x < 2).head()\n" +
        " ..throws exception", 
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan0).takeWhile(lessThan2).head(), 
        new java.util.NoSuchElementException());
    we.expect(" ..causes zero evaluation of x -> x + 1",
        incrHistory, List.of()); 
    we.expect(" ..causes one evaluation of x -> x < 0",
        lessThan0History, List.of(1)); 
    we.expect(" ..causes zero evaluation of x -> x < 2",
        lessThan2History, List.of()); 

    List<Integer> lessThan5History = new ArrayList<>();
    BooleanCondition<Integer> lessThan5 = x -> { 
      lessThan5History.add(x);
      return x < 5;
    };

    incrHistory.retainAll(List.of());
    lessThan2History.retainAll(List.of());
    we.expectException("InfiniteList.iterate(1, x -> x + 1).takeWhile(x -> x < 5)" +
        ".takeWhile(x -> x < 2).tail().head()\n" +
        " ..throws exception",
        () -> InfiniteList.iterate(1, incr).takeWhile(lessThan5).takeWhile(lessThan2).tail().head(),
        new java.util.NoSuchElementException());
    we.expect(" ..causes one evaluation of x -> x + 1",
        incrHistory, List.of(1)); 
    we.expect(" ..causes two evaluations of x -> x < 5",
        lessThan5History, List.of(1, 2)); 
    we.expect(" ..causes two evaluations of x -> x < 2",
        lessThan2History, List.of(1, 2)); 

    List<Integer> isEvenHistory = new ArrayList<>();
    BooleanCondition<Integer> isEven = x -> { 
      isEvenHistory.add(x);
      return x % 2 == 0;
    };

    List<Integer> lessThan10History = new ArrayList<>();
    BooleanCondition<Integer> lessThan10 = x -> { 
      lessThan10History.add(x);
      return x < 10;
    };
    incrHistory.retainAll(List.of());
    lessThan10History.retainAll(List.of());
    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0)" +
        ".takeWhile(x -> x < 10).head()\n" +
        " ..returns 2",
        () -> InfiniteList.iterate(1, incr).filter(isEven).takeWhile(lessThan10).head(), 2);
    we.expect(" ..causes one evaluation of x -> x + 1",
        incrHistory, List.of(1)); 
    we.expect(" ..causes two evaluations of x -> x % 2 == 0",
        isEvenHistory, List.of(1, 2)); 
    we.expect(" ..causes one evaluations of x -> x < 10",
        lessThan10History, List.of(2)); 

    incrHistory.retainAll(List.of());
    isEvenHistory.retainAll(List.of());
    lessThan10History.retainAll(List.of());
    we.expect("InfiniteList.iterate(1, x -> x + 1).filter(x -> x % 2 == 0)" +
        ".takeWhile(x -> x < 10).tail().head()\n" +
        " ..returns 4",
        () -> InfiniteList.iterate(1, incr).filter(isEven).takeWhile(lessThan10)
        .tail().head(), 4);
    we.expect(" ..causes three evaluations of x -> x + 1",
        incrHistory, List.of(1, 2, 3)); 
    we.expect(" ..causes four evaluations of x -> x % 2 == 0",
        isEvenHistory, List.of(1, 2, 3, 4)); 
    we.expect(" ..causes two evaluations of x -> x < 10",
        lessThan10History, List.of(2, 4)); 

    incrHistory.retainAll(List.of());
    lessThan10History.retainAll(List.of());
    InfiniteList<Integer> list = InfiniteList.iterate(1, incr).takeWhile(lessThan10);
    we.expect("InifiniteList<Integer> list = InfiniteList.iterate(1, x -> x + 1)" +
        ".takeWhile(x -> x < 10)\n" +
        " ..list.tail().tail().head() returns 3",
        list.tail().tail().head(), 3);
    list.head();
    we.expect(" ..list.head() causes zero evaluation of x -> x < 10",
        lessThan10History, List.of(1, 2, 3));
    list.tail().head();
    we.expect(" ..list.tail().head() causes zero evaluation of x -> x < 10",
        lessThan10History, List.of(1, 2, 3));
    list.tail().tail().tail().head();
    we.expect(" ..list.tail().tail().tail().head() causes one evaluation of x -> x < 10",
        lessThan10History, List.of(1, 2, 3, 4));
  }
}