import cs2030s.fp.BooleanCondition;
import cs2030s.fp.Consumer;
import cs2030s.fp.Maybe;
import cs2030s.fp.Producer;
import cs2030s.fp.Transformer;
import java.util.ArrayList;
import java.util.List;

public class Test5 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    Producer<Double> zero = new Producer<>() {
      public Double produce() {
        return 0.0;
      }
    };
    we.expect("Maybe.<Number>none().orElse(zero);",
        Maybe.<Number>none().orElse(zero), 0.0);
    we.expect("Maybe.<Number>some(1).orElse(zero);",
        Maybe.<Number>some(1).orElse(zero), 1);

    List<Object> list = new ArrayList<>();
    Consumer<Object> addToList = new Consumer<>() {
      public void consume(Object o) {
        list.add(o);
      }
    };

    Maybe.<Number>none().ifPresent(addToList);
    we.expect("Maybe.<Number>none().ifPresent(addToList) does nothing",
        list.size(), 0);
    Maybe.<Number>some(1).ifPresent(addToList);
    we.expect("Maybe.<Number>some(1).ifPresent(addToList) add 1 to list",
        list.get(0), 1);
  }
}