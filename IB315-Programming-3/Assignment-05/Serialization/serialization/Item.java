package serialization;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

import java.io.Serializable;

public class Item implements Comparable<Item>, Serializable {

  private static final long serialVersionUID = 6632932832422932774L;

public enum Category { 
    VEGETABLE, DAIRY, CANDY, TOILETRY
  }

  private final String name;
  private final int price;
  private final Category category;


  public Item(String name, Category category, int price){
    this.name = name;
    this.category = category;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public Category getCategory() {
    return category;
  }

  @Override
  public String toString() {
    return String.format("%s (%d EUR), %s", 
        name, price, category);
  }



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + price;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Item other = (Item) obj;
    if (category != other.category)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (price != other.price)
      return false;
    return true;
  }

  @Override
  public int compareTo(Item other) {
    if (other==null)
      throw new IllegalArgumentException("other item must not be null");

    int result = name.compareTo(other.getName());

    if (result == 0)
      result = Integer.compare(price, other.getPrice());

    if (result == 0)
      result = category.compareTo(other.getCategory());

    return result;
  }

  public static void readFromInput(List<Item> items){
    try (Scanner in = new Scanner(System.in)){

      while (true) {

        String yesNo = "";

        while (!yesNo.toUpperCase().equals("N") && !yesNo.toUpperCase().equals("J")){
          out.print("Wollen Sie ein Produkt eingeben (j/n)? ");
          yesNo = in.nextLine();
        }

        if (yesNo.toUpperCase().equals("N"))
          break;

        String name = "";
        while (name.isBlank()){
          out.print("Name des Produkts: ");
          name = in.nextLine();
        }


        out.println("Produktkategorien: ");
        for (Category category : Category.values()){
          out.printf(" %d - %s%n", category.ordinal(), category.name());
        }

        int categoryOrdinal = -1;

        while (categoryOrdinal < 0 || categoryOrdinal >= Category.values().length){
          out.print("Nummer der Produktkategorie: ");
          categoryOrdinal = in.nextInt();
          in.nextLine();
        }

        Category category = Category.values()[categoryOrdinal];

        int price = 0;

        while (price <= 0){
          out.print("Preis in EUR (> 0): ");
          price = in.nextInt();
          in.nextLine();
        }

        items.add(new Item(name, category, price));

        out.println("Produkt hinzugefügt\n");
      }

    }
  }

}
