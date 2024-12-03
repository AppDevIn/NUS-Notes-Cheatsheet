import cs2030s.fp.BooleanCondition;
import cs2030s.fp.Maybe;

public class Test3 {
  public static void main(String[] args) {
    CS2030STest we = new CS2030STest();

    BooleanCondition<Integer> isEven = new BooleanCondition<>() {
      public boolean test(Integer x) {
        return x % 2 == 0;
      }
    };
    we.expect("Maybe.<Integer>none().filter(isEven) is Maybe.none()", 
        Maybe.<Integer>none().filter(isEven),
        Maybe.none()); 
    we.expect("Maybe.<Integer>some(null).filter(isEven) is Maybe.none()",
        Maybe.<Integer>some(null).filter(isEven),
        Maybe.none());
    we.expect("Maybe.<Integer>some(1).filter(isEven) is Maybe.none()", 
        Maybe.<Integer>some(1).filter(isEven),
        Maybe.none());
    we.expect("Maybe.<Integer>some(2).filter(isEven) is Maybe.some(2)", 
        Maybe.<Integer>some(2).filter(isEven),
        Maybe.some(2));
  }
}