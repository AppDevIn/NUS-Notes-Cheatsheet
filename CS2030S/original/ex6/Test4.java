import cs2030s.fp.Combiner;
import cs2030s.fp.Lazy;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

class Test4 {
  /**
   * Main method for Test4.
   *
   * @param args Ignored and unused command line arguments.
   */
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    Lazy<Integer> ten = Lazy.of(10);
    Lazy<Integer> five = Lazy.of(5);
    Lazy<Integer> fifty = five.combine(ten, (x, y) -> x * y);
    we.expect("Lazy<Integer> fifty = Lazy.of(5).combine(Lazy.of(10), (x, y) -> x * y);\n" +
        "fifty.toString() returns ?",
        fifty.toString(), "?");
    we.expect("fifty.get() returns 50",
        fifty.get(), 50);
    Lazy<Integer> hundred = fifty.combine(fifty, (x, y) -> x + y);
    we.expect("fifty.combine(fifty, (x, y) -> x + y).toString() returns ?",
        hundred.toString(), "?");
    we.expect("fifty.combine(fifty, (x, y) -> x + y).toString() returns ?",
        hundred.get(), 100);

    Combiner<Integer, Double, String> f = (x, y) -> Integer.toString(x) + " " + Double.toString(y);
    Lazy<String> str = Lazy.of(10).combine(Lazy.of(0.01), f);
    we.expect("Lazy<String> str = Lazy.of(10).combine(Lazy.of(0.01), (x, y) -> x + \" \" + y)\n" + 
        "str.toString() returns ?",
        str.toString(), "?");
    we.expect("s.toString() returns ? again",
        str.toString(), "?");
    we.expect("s.get() returns \"10 0.01\"",
        str.get(), "10 0.01");

    we.expectCompile("Lazy<Number> n = " +
        "Lazy.<String>of(\"hello\").combine(Lazy.<Integer>of(123), " + 
        "(Object x, Object y) -> (Integer)(x.hashCode() + y,hashCode())); should compile",
        "cs2030s.fp.Combiner<Object,Object,Integer> f = (x, y) -> x.hashCode() + y.hashCode(); " +
        "cs2030s.fp.Lazy<Number> n = cs2030s.fp.Lazy.<String>of(\"hello\")" + 
        "    .combine(cs2030s.fp.Lazy.<Integer>of(123), f);",
        true);
  }
}