// @author Axxxxxxx
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Q8 {
  public static List<FruitStall<? extends Fruit>> sortByShortestExpiry(List<FruitStall<? extends Fruit>> stall) {
    return stall.stream().sorted(new Q7<>()).toList();
  }
}
