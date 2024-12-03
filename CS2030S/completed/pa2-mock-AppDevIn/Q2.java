// @author A0302149M
import cs2030s.fp.Maybe;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;

public class Q2 {
  public static List<String> findBusStops(List<Bus> buses) {
    return buses.stream()
      .flatMap(x -> x.getStops().stream())
      .filter(x -> x.getNumber() % 2 == 0)
      .map(x -> x.getName())
      .toList();

  }
}
