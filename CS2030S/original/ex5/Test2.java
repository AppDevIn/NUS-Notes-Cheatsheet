import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.Map;

public class Test2 {

  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectCompileWithImport("Maybe.None m is not compilable", 
        "MMaybe.None m;", 
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer; ",
        false);
    we.expectCompileWithImport("Maybe.none() is compilable",
        "Maybe<Integer> m = Maybe.none();",
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer;",
        true);
    we.expectCompileWithImport("Maybe.none.get() does not exist",
        "Maybe.some(0).get();",
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer;",
        false);
    
    we.expect("Maybe.none() is []",
        Maybe.none().toString(),
        "[]");
    we.expect("Maybe.none() == Maybe.none() is true",
        Maybe.none() == Maybe.none(),
        true);
    we.expect("Maybe.none().equals(Maybe.none()) is true",
        Maybe.none().equals(Maybe.none()),
        true);
    we.expect("Maybe.none().equals(Maybe.some(\"day\")) is false",
        Maybe.none().equals(Maybe.some("day")),
        false);
    we.expect("Maybe.none().equals(Maybe.some(null)) is false",
        Maybe.none().equals(Maybe.some(null)),
        false);
    we.expect("Maybe.some(null).equals(Maybe.none()) is false",
        Maybe.some(null).equals(Maybe.none()),
        false);
        
    we.expect("Maybe.of(null).equals(Maybe.none()) is true",
        Maybe.of(null).equals(Maybe.none()),
        true);
    we.expect("Maybe.of(null) == Maybe.none() is true",
        Maybe.of(null) == Maybe.none(),
        true);
    we.expect("Maybe.of(null).equals(Maybe.some(null)) is false",
        Maybe.of(null).equals(Maybe.some(null)),
        false);
    we.expect("Maybe.of(4).equals(Maybe.none()) is false",
        Maybe.of(4).equals(Maybe.none()),
        false);
    we.expect("Maybe.of(4).equals(Maybe.some(4)) is true",
        Maybe.of(4).equals(Maybe.some(4)),
        true);

    Transformer<Integer, Integer> incr = new Transformer<>() {
      @Override
      public Integer transform(Integer x) {
        return x + 1;
      }
    };

    we.expect("Maybe.<Integer>none().map(incr) is Maybe.none()",
        Maybe.<Integer>none().map(incr),
        Maybe.none());
    we.expectException("Maybe.<Integer>some(null).map(incr) throws NullPointerException", 
        () -> Maybe.<Integer>some(null).map(incr), new NullPointerException());
    we.expect("Maybe.<Integer>some(1).map(incr) is Maybe.some(2)",
        Maybe.<Integer>some(1).map(incr),
        Maybe.some(2));

    Map<String, Integer> map = Map.of("one", 1, "two", 2);
    Transformer<String, Integer> wordToInt = new Transformer<>() {
      public Integer transform(String x) {
        return map.get(x);
      }
    };
    we.expect("Maybe.<String>none().map(wordToInt) is Maybe.none()",
        Maybe.<String>none().map(wordToInt),
        Maybe.none());
    we.expect("Maybe.<String>some(\"\").map(wordToInt) is Maybe.some(null)",
        Maybe.<String>some("").map(wordToInt),
        Maybe.some(null));
    we.expect("Maybe.<String>some(\"one\").map(wordToInt) is Maybe.some(1)",
        Maybe.<String>some("one").map(wordToInt),
        Maybe.some(1));

    Transformer<String, Maybe<Integer>> wordToMaybeInt = new Transformer<>() {
      public Maybe<Integer> transform(String x) {
        return Maybe.of(map.get(x));
      }
    };

    we.expect("Maybe.<String>none().map(wordToMaybeInt) is Maybe.none()", 
        Maybe.<String>none().map(wordToMaybeInt),
        Maybe.none());
    we.expect("Maybe.<String>some(\"\").map(wordToMaybeInt) is Maybe.some(Maybe.none())",
        Maybe.<String>some("").map(wordToMaybeInt),
        Maybe.some(Maybe.none()));
    we.expect("Maybe.<String>some(\"one\").map(wordToMaybeInt) is Maybe.some(Maybe.some(1))", 
        Maybe.<String>some("one").map(wordToMaybeInt),
        Maybe.some(Maybe.some(1)));
  }
}