package shelf;

public class Tool extends ShelfItem{
	
	private final String name;
	
	public Tool(String name) {
		if(name== null || name.isEmpty())
			throw new IllegalArgumentException();
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Tool [name=" + name + "]";
	}
	
	
}
