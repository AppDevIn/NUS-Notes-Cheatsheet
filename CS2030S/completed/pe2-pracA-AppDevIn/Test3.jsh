/open Named.java
/open Perishable.java
/open Fruit.java
/open FruitStall.java
/open Q4.java
/open Q5.java
/open Q6.java
/open Q7.java
/open Q8.java

/open Fruits.jsh

FruitStall<Apple> fs1 = new FruitStall<>(List.of(new Apple(1), new Apple(9)));
FruitStall<Fruit> fs2 = new FruitStall<>(List.of(new Apple(2)));
Q4.mergeStalls(fs1, fs2);

Q4.mergeStalls(fs1, new FruitStall<>());

FruitStall<Fruit> fs = Q4.mergeStalls(fs1, fs2); 
FruitStall<Apple> fs = Q4.mergeStalls(fs1, fs2);  // error

FruitStall<Fruit> fs3 = new FruitStall<>(List.of(new Apple(8), new Mango()));
FruitStall<Mango> fs4 = new FruitStall<>(List.of(new Mango()));
Q5.findUniqueFruitTypes(List.of(fs1, fs2, fs3, fs4));
Q5.findUniqueFruitTypes(List.of(fs4, fs3, fs2, fs1));
Q5.findUniqueFruitTypes(List.of(fs4));
                                                      
FruitStall<Devil> fs5 = new FruitStall<>(List.of(new Devil(), new Devil()));

Q6.consolidateStallsbyType(List.of(fs3, fs4, fs5)); 

new Q7<>().compare(fs3, fs4) == 0
new Q7<>().compare(fs4, fs5) < 0
new Q7<>().compare(fs5, fs4) > 0

Q8.sortByShortestExpiry(List.of(fs1, fs2, fs3, fs4, fs5));
