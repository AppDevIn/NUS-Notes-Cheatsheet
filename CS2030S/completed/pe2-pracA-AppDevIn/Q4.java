// @author Axxxxxxx
import java.util.List;
import java.util.stream.Stream;


public class Q4<T extends Fruit> {
  FruitStall<? extends T> cf;

  public static <T extends Fruit> FruitStall<T> mergeStalls(FruitStall<? extends T> f1, FruitStall<? extends T> f2) {
   
    return new FruitStall<T>(Stream.concat(f1.getFruits().stream(), f2.getFruits().stream()).toList());
  }
}
