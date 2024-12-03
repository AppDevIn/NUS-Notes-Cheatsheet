/open Named.java
/open Perishable.java
/open Fruit.java

new Fruit(() -> "Apple", () -> 5);
new Fruit(() -> "Apple", () -> 5).getName();
new Fruit(() -> "Apple", () -> 5).getDaysToExpiry();
