// @author Axxxxxxx
import java.util.List;
import java.util.stream.Stream;


public class Q5 {
  public static List<String> findUniqueFruitTypes(List<FruitStall<? extends Fruit>> fruitStall) {
    return fruitStall.stream().flatMap(t -> t.getFruits().stream()).flatMap(t -> Stream.of(t.getName())).distinct().sorted().toList();
  }
}
