import cs2030s.fp.Lazy;
import cs2030s.fp.Producer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Test1 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    we.expectCompile("Lazy<Integer> eight = Lazy.of(8) type checks",
        "cs2030s.fp.Lazy<Integer> eight = cs2030s.fp.Lazy.of(8)", true);
    we.expect("Lazy.of(8) has a string reprentation of 8",
        Lazy.of(8).toString(), "8");
    we.expect("Lazy.of(8).get() returns 8",
        Lazy.of(8).get(), 8);
    we.expectCompile("Lazy<String> hello = Lazy.of(() -> \"hello\") type checks",
        "cs2030s.fp.Producer<String> p = () -> \"hello\";" + 
        "cs2030s.fp.Lazy<String> hello = cs2030s.fp.Lazy.of(p);", 
        true);
    we.expectCompile("Lazy<Object> hello = Lazy.of(() -> \"hello\") type checks",
        "cs2030s.fp.Producer<String> p = () -> \"hello\";" + 
        "cs2030s.fp.Lazy<Object> hello = cs2030s.fp.Lazy.of(p)", 
        true);

    Producer<String> s = () -> "hello";
    Lazy<String> hello = Lazy.of(s);
    we.expect("hello has a string reprentation of ?",
        hello.toString(), "?");
    we.expect("hello.get() returns \"hello\"",
        hello.get(), "hello");

    List<Integer> numOfCalls = new ArrayList<>();
    // add side effects
    s = () -> { 
      numOfCalls.add(1); 
      return "hello"; 
    };

    hello = Lazy.of(s);
    hello.get();
    hello.get();
    we.expect("hello.get() calls the producer exactly once",
        numOfCalls.size(), 1);

    Random rng = new Random(1);
    Producer<Integer> r = () -> rng.nextInt();
    Lazy<Integer> random = Lazy.of(r);
    we.expect("random has a string reprentation of ?",
        random.toString(), "?");
    we.expect("random.get() twice returns the same value",
        random.get(), random.get());

    Lazy<Object> n = Lazy.of(null);
    we.expect("Lazy.of(null).toString() returns \"null\"",
        Lazy.of((Object) null).toString(), "null");
    we.expect("Lazy.of(null).get() returns null",
        Lazy.of((Object) null).get(), null);
    we.expect("Lazy.of(()->null).toString() returns ?",
        Lazy.of((Producer<Integer>) () -> null).toString(), "?");
    we.expect("Lazy.of(()->null).get() returns null",
        Lazy.of((Producer<Integer>) () -> null).get(), null);
  }
}