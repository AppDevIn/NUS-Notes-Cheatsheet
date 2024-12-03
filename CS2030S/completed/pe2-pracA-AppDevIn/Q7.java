// @author Axxxxxxx
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Q7<T extends Fruit> implements Comparator<FruitStall<? extends T>> {
  @Override 
  public int compare(FruitStall<? extends T> f1, FruitStall<? extends T> f2) {
   return f1.getFruits().stream().map(x ->
           x.getDaysToExpiry())
           .reduce(Integer.MAX_VALUE, (x, y) -> Math.min(x, y)
                   - f2.getFruits().stream()
                   .map(x2 -> x2.getDaysToExpiry())
                   .reduce(Integer.MAX_VALUE, (t, z) -> Math.min(t, z)));
  }
}
