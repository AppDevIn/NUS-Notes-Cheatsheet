import cs2030s.fp.BooleanCondition;
import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;
import java.util.Map;

public class Test4 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    Map<String, Integer> map = Map.of("one", 1, "two", 2);
    Transformer<String, Maybe<Integer>> wordToMaybeInt = new Transformer<>() {
      public Maybe<Integer> transform(String x) {
        return Maybe.of(map.get(x));
      }
    };

    we.expect("Maybe.<String>none().flatMap(wordToMaybeInt) is Maybe.none()",
        Maybe.<String>none().flatMap(wordToMaybeInt), Maybe.none());
    we.expect("Maybe.<String>some(\"\").flatMap(wordToMaybeInt) is Maybe.none()",
        Maybe.<String>some("").flatMap(wordToMaybeInt), Maybe.none());
    we.expect("Maybe.<String>some(\"one\").flatMap(wordToMaybeInt) is Maybe.some(1)",
        Maybe.<String>some("one").flatMap(wordToMaybeInt), Maybe.some(1));
    we.expectCompileWithImport(
        "Maybe<Number> m = Maybe.<String>some(\"one\").flatMap(t) should compile",
        "Transformer<String,Maybe<Integer>> t = new Transformer<>() {" +
        "  public Maybe<Integer> transform(String x) {" +
        "    return Maybe.of(1);" +
        "  }" +
        "};" +
        "Maybe<Number> m = Maybe.<String>some(\"one\").flatMap(t)",
        "import cs2030s.fp.*;",
        true);
  }
}