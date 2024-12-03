// @author Axxxxxxx
import java.util.List;

public class FruitStall<T extends Fruit> {
  private List<? extends T> fruits;
  
  public FruitStall() {
    this.fruits = List.of();
  }

  public FruitStall(List<? extends T> fruits) {
    this.fruits = fruits;
  }

  public List<? extends T> getFruits() {
    return this.fruits;
  }

  public List<? extends T> findFruitsByName(String name) {
    List<T> list = List.of();
    this.fruits.stream().filter(i -> i.getName().equals(name)).forEach(i -> list.add(i));
    return list;
    //return this.fruits.stream().filter(i -> i.getName().equals(name)).toList();
  }

  @Override 
  public String toString() {
    return this.fruits.stream().reduce("", (i, acc) ->  String.valueOf(i) + "\n- " +acc,  (x, y) -> x + y) + "\n";
  }
}
