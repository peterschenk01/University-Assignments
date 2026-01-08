package shelf;

import org.junit.*;
import static org.junit.Assert.*;

public class ToolTest {
	
	@Test
	public void toolConstructorInitialize(){
	    Tool tool = new Tool("Säge");
	    assertEquals("Säge", tool.getName());
	}
	  
	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorNullName(){
	    new Tool(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void bookConstructorEmptyName(){
	    new Tool("");
	}
}
