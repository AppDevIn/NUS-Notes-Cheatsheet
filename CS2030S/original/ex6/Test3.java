import cs2030s.fp.BooleanCondition;
import cs2030s.fp.Lazy;
import cs2030s.fp.Producer;
import java.util.ArrayList;
import java.util.List;

class Test3 {

  /**
   * Main method for Test3.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    Lazy<Boolean> even = Lazy.of(50).filter(x -> x % 2 == 0);
    we.expect("Lazy.of(50).filter(x -> x % 2 == 0).toString() returns ?",
        even.toString(), "?");
    we.expect("Lazy.of(50).filter(x -> x % 2 == 0).get() returns true",
        even.get(), true);

    we.expect("Lazy.of(50).equals(Lazy.of(5).map(x -> x * 10)) returns true",
        Lazy.of(50).equals(Lazy.of(5).map(x -> x * 10)), true);
    we.expect("Lazy.of(50).equals(50)",
        Lazy.of(50).equals(50), false);
    we.expect("Lazy.of(50).equals(Lazy.of(\"50\")",
        Lazy.of(50).equals(Lazy.of("50")), false);
    we.expect("Lazy.of(50).filter(i -> i % 2 == 0).equals(Lazy.of(true))",
        Lazy.of(50).filter(x -> x % 2 == 0).equals(Lazy.of(true)), true);

    List<Integer> numOfEvals = new ArrayList<>();
    BooleanCondition<String> isHello = s -> {
      numOfEvals.add(1);
      return s.equals("hello");
    };
    Lazy<Boolean> same = Lazy.of("hi").filter(isHello);
    we.expect("Lazy.of(\"hi\").filter(isHello).toString() returns ?", 
        same.toString(), "?");
    we.expect("Lazy.of(\"hi\").filter(isHello).toString() does not evaluate the boolean condition", 
        numOfEvals.size(), 0);
    we.expect("Lazy.of(\"hi\").filter(isHello).get() returns false", 
        same.get(), false);
    we.expect("Lazy.of(\"hi\").filter(isHello).get() evaluates the boolean condition once", 
        numOfEvals.size(), 1);
    we.expect("Lazy.of(\"hi\").filter(isHello).get() " +
        "does not evaluate the boolean condition again", 
        numOfEvals.size(), 1);

    we.expectCompile("Lazy<Boolean> same = Lazy.<Integer>of(4).filter(alwaysFalse) should compile",
        "cs2030s.fp.BooleanCondition<Object> alwaysFalse = s -> false; " + 
        "cs2030s.fp.Lazy<Boolean> same = cs2030s.fp.Lazy.<Integer>of(4).filter(alwaysFalse);",
        true);
    
    Producer<String> producer = () -> "123456";
    Lazy<String> oneToSix = Lazy.of(producer);
    we.expect("oneToSix is unevaluated", 
        oneToSix.toString(), "?");
    we.expect("oneToSix == oneToSix returns true", 
        oneToSix == oneToSix, true);
    we.expect("oneToSix is unevaluated", 
        oneToSix.toString(), "?");
    we.expect("oneToSix.equals(oneToSix) returns true", 
        oneToSix.equals(oneToSix), true);
    we.expect("oneToSix is now evaluated", 
        oneToSix.toString(), "123456");
    
    System.out.println("-----");
    System.out.println("Lazy<Boolean> odd = Lazy.of(50).filter(x -> x % 2 == 1);");
    System.out.println("-----");

    Lazy<Boolean> odd = Lazy.of(50).filter(x -> x % 2 == 1);
    we.expect("odd.toString() returns ?",
        odd.toString(), "?");
    we.expect("odd == odd returns true",
        odd == odd, true);
    we.expect("odd.toString() returns false (odd is still not evaluated)",
        odd.toString(), "?");
    we.expect("odd.equals(odd) returns true",
        odd.equals(odd), true);
    we.expect("odd.toString() returns false (odd is now evaluated)",
        odd.toString(), "false");
  }
}