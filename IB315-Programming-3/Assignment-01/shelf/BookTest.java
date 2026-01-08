package shelf;

import org.junit.*;
import static org.junit.Assert.*;

public class BookTest {

	@Test
	public void testBook_Constructor() {
		Book book = new Book("Titel", "Autor", 69);
		
		assertEquals(book.getTitle(), "Titel");
		assertEquals(book.getAuthor(), "Autor");
		assertEquals(book.getPages(), 69);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void BookConstructorNullTitle() {
		new Book(null, "Autor", 69);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorEmptyTitle(){
	    new Book("", "Autor", 69);
	}

	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorNullAuthor(){
	    new Book("Titel", null, 69);
	}

	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorEmptyAuthor(){
	    new Book("Titel", "", 69);
	}

	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorZeroPages(){
	    new Book("Titel", "Autor", 0);
	}
	  
	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorNegativePages(){
	    new Book("Titel", "Autor", -1);
	}
}
