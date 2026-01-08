package shelf;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ShelfTest {
	
	private Book[] books;
	private Shelf<Book> bookShelf;
	
	@Before
	public void initializeBooks() {
		books = new Book[4];
		books[0] = new Book("Ullenbloom", "Java ist auch eine Insel", 1246);
	    books[1] = new Book("Schuld", "Ferdinand von Schirach", 208);
	    books[2] = new Book("Bibi und Tina: Pferdeabenteuer am Meer", "Matthias von Bornstädt", 34);
	    books[3] = new Book("Alice im Wunderland", "Lews Carroll", 234);
	    bookShelf = new Shelf<>();
	    
	    for(int i = 0; i < 4; i++)
	    	bookShelf.set(i, books[i]);
	}
	
	@Test
	public void shelfConstructor() {
		Shelf<ShelfItem> shelf = new Shelf<ShelfItem>();
		assertNull(shelf.getUpperLeft());
	    assertNull(shelf.getUpperRight());
	    assertNull(shelf.getLowerLeft());
	    assertNull(shelf.getLowerRight());
	}
	
	@Test
	public void shelfSetIndex() {
		Shelf<Book> shelf = new Shelf<Book>();
		
		for(int i = 0; i < 4; i++)
			shelf.set(i, books[i]);
		
		for(int i = 0; i < 4; i++)
			assertSame(books[i], shelf.get(i));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shelfSetIndexTooLargeIndex(){
	    Shelf<Book> shelf = new Shelf<>();
	    shelf.set(4, books[0]);
	}

	@Test(expected=IllegalArgumentException.class)
	public void shelfSetIndexNegativeIndex(){
	    Shelf<Book> shelf = new Shelf<>();
	    shelf.set(-1, books[0]);
	}
	
	@Test
	public void ShelfIterator() {
		Shelf<Book> shelf = new Shelf<Book>();
		
		for(int i = 0; i < 4; i++)
			shelf.set(i, books[i]);
		
		int i = 0;
		for(Book book : shelf)
			assertSame(books[i++], book);
	}
	
	@Test
	public void ShelfIteratorBeyondLast() {
		Shelf<Book> shelf = new Shelf<Book>();
		Iterator<Book> it = shelf.iterator();
		
		for(int i = 0; i < 4; i++) {
			assertTrue(it.hasNext());
			it.next();
		}
		
		assertFalse(it.hasNext());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void shelfIteratorBeyondLastException() {
		Shelf<Book> shelf = new Shelf<Book>();
		Iterator<Book> it = shelf.iterator();
		
		for (int i = 0; i < 4; i++){
			try{
				it.next();
			} catch (NoSuchElementException e){
				fail("Next must not throw NoSuchElementException before the last element");
			}
		}

		it.next();
	}
	
	@Test
	public void shelfTakeFrom() {
		Shelf<Book> shelf = new Shelf<Book>();
		
		shelf.takeFrom(bookShelf);
		
		for(int i = 0; i < 4; i++)
			assertSame(shelf.get(i), books[i]);
		
		for(int i = 0; i < 4; i++)
			assertNull(bookShelf.get(i));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shelfTakeFromNull() {
		bookShelf.takeFrom(null);
	}
	
	@Test
	public void ShelfMax() {
		bookShelf.set(2, null);
		
		Book max = bookShelf.max((b1, b2) -> Integer.compare(b1.getPages(), b2.getPages()));
		
		assertSame(max, books[0]);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ShelfMaxNullComparator() {
		bookShelf.max(null);
	}
	
	@Test
	public void ShelfTransferAndTrim() {
		bookShelf.set(2, null);
		Shelf<Book> bookShelf2 = new Shelf<Book>();
		
		for(int i = 0; i < 4; i++)
	    	bookShelf2.set(i, books[i]);
		
		Shelf.transferAndTrim(bookShelf, bookShelf2);
		
		assertSame(books[0], bookShelf2.get(0));
		assertSame(books[1], bookShelf2.get(1));
		assertSame(books[3], bookShelf2.get(2));
		assertNull(bookShelf2.get(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shelfTransferAndTrimFromNull() {
		Shelf.transferAndTrim(null, bookShelf);
	}

	@Test(expected=IllegalArgumentException.class)
	public void shelfTransferAndTrimToNull() {
		Shelf.transferAndTrim(bookShelf, null);
	}
}
