package shelf;

public class ShelfMain {

	public static void main(String[] args) {
		Shelf<Book> bookShelf = new Shelf<Book>();
		
		bookShelf.setUpperLeft(new Book("Ullenbloom", "Java ist auch eine Insel", 1246));
	    bookShelf.setUpperRight(new Book("Schuld", "Ferdinand von Schirach", 208));
	    bookShelf.setLowerRight(new Book("Bibi und Tina", "Matthias von Bornstädt", 34));
	    
	    System.out.println("bookShelf:");
	    printShelf(bookShelf);
	    
	    Shelf<Tool> toolShelf = new Shelf<Tool>();
	    
	    toolShelf.setUpperLeft(new Tool("Schraubenzieher"));
	    toolShelf.setLowerRight(new Tool("Säge"));
	    
	    System.out.println("toolShelf:");
	    printShelf(toolShelf);
	    
		Shelf<Book> newBookShelf = new Shelf<Book>();
		
		newBookShelf.takeFrom(bookShelf);
		
		System.out.println("newBookShelf:");
		printShelf(newBookShelf);
		
		Shelf<ShelfItem> generalShelf = new Shelf<ShelfItem>();
		
		generalShelf.takeFrom(newBookShelf);
		
		System.out.println("generalShelf:");
		printShelf(generalShelf);
		
		bookShelf.setUpperLeft(new Book("Ullenbloom", "Java ist auch eine Insel", 1246));
	    bookShelf.setUpperRight(new Book("Schuld", "Ferdinand von Schirach", 208));
	    bookShelf.setLowerRight(new Book("Bibi und Tina", "Matthias von Bornstädt", 34));
	    
	    Book max = bookShelf.max((b1, b2) -> Integer.compare(b1.getPages(), b2.getPages()));
	    System.out.println("Buch mit den meisten Seiten aus bookShelf: " + max);
	    
	    Shelf.transferAndTrim(bookShelf, newBookShelf);
	    Shelf.transferAndTrim(newBookShelf, generalShelf);
	}

	public static void printShelf(Shelf<?> shelf) {
		for(var item : shelf)
			System.out.println(item);
	}
}
