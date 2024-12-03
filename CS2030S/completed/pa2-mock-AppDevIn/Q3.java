// @author A0302149M
import cs2030s.fp.Maybe;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;

public class Q3 {
  public static Maybe<String> longestOdd(List<Bus> buses) {
    return buses.stream().
      flatMap(bus -> bus.getStops().stream())
      .filter(x -> x.getNumber() % 2 != 0)
      .sorted((x, y) -> y.getName().length() - x.getName().length())
      .findFirst()
      .map(BusStop::getName)
      .map(Maybe::of)
      .orElse(Maybe.none());
  }
}
