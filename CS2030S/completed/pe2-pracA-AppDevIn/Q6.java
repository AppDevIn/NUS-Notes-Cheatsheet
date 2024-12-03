// @author Axxxxxxx
import java.util.List;
import java.util.stream.Stream;

public class Q6 {
  public static List<FruitStall<Fruit>> consolidateStallsbyType(List<FruitStall<? extends Fruit>> stall) {
    return stall.stream()
      .flatMap(x -> x.getFruits().stream())
      .map(x -> x.getName())
      .distinct()
      .sorted()
      .map(f -> new FruitStall<Fruit>(
            stall.stream().flatMap(x -> x.getFruits().stream())
            .filter(x -> x.getName().equals(f)).toList()
      )).toList();
  }
}
