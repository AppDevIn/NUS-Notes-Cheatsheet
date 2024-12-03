import cs2030s.fp.Maybe;
import cs2030s.fp.Transformer;

public class Test1 {

  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectCompileWithImport("new Maybe<>() is not compilable", 
        "Maybe<Object> maybe = new Maybe<>();", 
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer; ",
        false);
    we.expectCompileWithImport("Maybe.Some<Object> is not accessible",
        "Maybe.Some<Object> m;",
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer;",
        false);
    we.expectCompileWithImport("Maybe.some(0) is compilable",
        "Maybe<Integer> m = Maybe.some(0);",
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer;",
        true);
    we.expectCompileWithImport("Maybe.some(0).get() does not exist",
        "Maybe.some(0).get();",
        "import cs2030s.fp.Maybe; import cs2030s.fp.Transformer;",
        false);
    
    we.expect("Maybe.some(null) is [null]",
        Maybe.some(null).toString(),
        "[null]");
    we.expect("Maybe.some(4) is [4]",
        Maybe.some(4).toString(),
        "[4]");
    we.expect("Maybe.some(\"day\").equals(Maybe.some(\"day\")) is true",
        Maybe.some("day").equals(Maybe.some("day")),
        true);
    we.expect("Maybe.some(null).equals(Maybe.some(\"day\")) is false",
        Maybe.some(null).equals(Maybe.some("day")),
        false);
    we.expect("Maybe.some(null).equals(Maybe.some(null)) is true",
        Maybe.some(null).equals(Maybe.some(null)),
        true);
    we.expect("Maybe.some(null).equals(null) is false",
        Maybe.some(null).equals(null),
        false);

    class AddOne implements Transformer<Integer, Integer> {
      @Override
      public Integer transform(Integer t) { 
        return t + 1; 
      }
    }

    class StrLen implements Transformer<String, Integer> {
      @Override
      public Integer transform(String t) { 
        return t.length(); 
      }
    }

    AddOne fn1 = new AddOne();
    StrLen fn2 = new StrLen();

    we.expect("Maybe.some(4).<Integer>map(fn1) is Maybe.some(5)",
        Maybe.some(4).<Integer>map(fn1),
        Maybe.some(5));
    we.expect("Maybe.some(5).map(fn1) is Maybe.some(6)",
        Maybe.some(5).map(fn1),
        Maybe.some(6));
    we.expect("Maybe.some(\"CS2030S\").map(fn2) is Maybe.some(7)",
        Maybe.some("CS2030S").map(fn2),
        Maybe.some(7));
    we.expect("Maybe.some(\"CS2030S\").map(fn2).map(fn1) is Maybe.some(8)",
        Maybe.some("CS2030S").map(fn2).map(fn1),
        Maybe.some(8));
    
    Maybe<Number> six = Maybe.some(4).map(fn1).map(fn1);
    we.expect("six is Maybe.some(6)",
        six,
        Maybe.some(6));

    class Destroyer implements Transformer<Integer, Object> {
      @Override
      public Object transform(Integer t) {
        return null;
      }
    }

    Destroyer fn3 = new Destroyer();
    we.expect("Maybe.some(4).map(fn3) is Maybe.some(null)",
        Maybe.some(4).map(fn3),
        Maybe.some(null));
    we.expect("Maybe.some(4).map(fn3) == Maybe.some(null) is false",
        Maybe.some(4).map(fn3) == Maybe.some(null),
        false);
    we.expect("Maybe.some(4).map(fn3).equals(Maybe.some(null)) is true",
        Maybe.some(4).map(fn3).equals(Maybe.some(null)),
        true);
  }
}