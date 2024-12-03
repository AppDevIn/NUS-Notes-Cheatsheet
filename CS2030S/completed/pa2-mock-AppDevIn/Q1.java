// @author A0302149M
import cs2030s.fp.Maybe;
import java.util.stream.Stream;

public class Q1 {
  public static long collatz(int n) {
    return Stream.iterate(n, x -> x != 1, y -> Maybe.some(y)
        .filter(x -> x % 2 == 0)
        .map(x -> x/2)
        .orElse(() -> y * 3 + 1)
        )
      .count();
  }
}
