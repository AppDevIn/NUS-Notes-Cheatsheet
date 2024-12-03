import cs2030s.fp.Lazy;
import cs2030s.fp.Producer;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

class Test2 {
  /**
   * Main method for Test2.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    Producer<String> producer = () -> "123456";
    Lazy<String> lazy = Lazy.of(producer);
    we.expect("Lazy<String> lazy = Lazy.of(() -> \"123456\")\n" +
        "lazy.map(s -> s.substring(0, 1)).toString() returns ?",
        lazy.map(s -> s.substring(0, 1)).toString(), "?");
    we.expect("lazy.toString() returns \"?\"",
        lazy.toString(), "?");
    we.expect("lazy.map(s -> s.substring(0, 1)).get() returns \"1\"",
        lazy.map(s -> s.substring(0, 1)).get(), "1");
    we.expect("lazy.toString() returns \"123456\"",
        lazy.toString(), "123456");

    List<Integer> numOfProduces = new ArrayList<>();
    List<Integer> numOfMaps = new ArrayList<>();
    producer = () -> { 
      numOfProduces.add(1); 
      return "123456"; 
    };
    Transformer<String, String> substr;
    substr = str -> { 
      numOfMaps.add(1); 
      return str.substring(0, 1); 
    };
    lazy = Lazy.of(producer);
    lazy = lazy.map(substr);
    lazy.get();
    lazy.get();
    we.expect("lazy.get(); lazy.get(); calls producer exactly once",
        numOfProduces.size(), 1); 
    we.expect("lazy.get(); lazy.get(); calls transformer exactly once",
        numOfMaps.size(), 1); 

    Lazy<Integer> lazyInt = Lazy.of(10);
    lazyInt = lazyInt.map(k -> k + 1);
    lazyInt = lazyInt.flatMap(j -> Lazy.of(j + 3));
    we.expect("Lazy.of(10).map(i -> i + 1).flatMap(j -> Lazy.of(j + 3)).toString() returns ?",
        lazyInt.toString(), "?");
    we.expect("Lazy.of(10).map(i -> i + 1).flatMap(j -> Lazy.of(j + 3)).get() returns 14",
        lazyInt.get(), 14);

    we.expectCompile("Lazy<Number> lazy = Lazy.<String>of(\"sunday\").map(" + 
        "(Object)x -> (Integer)x.hashCode()); should compile",
        "cs2030s.fp.Transformer<Object, Integer> hash = x -> x.hashCode(); " +
        "cs2030s.fp.Lazy<Number> lazy = cs2030s.fp.Lazy.<String>of(\"sunday\").map(hash);", 
        true);
    we.expectCompile("Lazy<Number> lazy = Lazy.<String>of(\"sunday\").flatMap(" +
        "(Object)x -> Lazy.<Integer>of(x.hashCode())); should compile",
        "cs2030s.fp.Transformer<Object, cs2030s.fp.Lazy<Integer>> hash = " +
        "x -> cs2030s.fp.Lazy.<Integer>of(x.hashCode());" +
        "cs2030s.fp.Lazy<Number> lazy = cs2030s.fp.Lazy.<String>of(\"sunday\").flatMap(hash); ",
        true);
  }
}