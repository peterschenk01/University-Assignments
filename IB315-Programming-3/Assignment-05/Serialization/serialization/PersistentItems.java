package serialization;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import serialization.Item.Category;

public class PersistentItems {

	/*private static List<Item> loadItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		try (DataInputStream in = new DataInputStream(new FileInputStream("items.bin"))){
			int a = in.readInt();
			for(int i = 0; i < a; i++) {
				String name = in.readUTF();
				char c = in.readChar();
				Category category = null;
				switch(c) {
					case 'V':
						category = Category.VEGETABLE;
						break;
					case 'D':
						category = Category.DAIRY;
						break;
					case 'C':
						category = Category.CANDY;
						break;
					case 'T':
						category = Category.TOILETRY;
						break;
					case 'X':
						in.skip(1);;
						break;
				}
				int price = in.readInt();
				
				items.add(new Item(name, category, price));
			}
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return items;
	}*/
	
	private static List<Item> loadItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("items.bin"))){
			int a = in.readInt();
			for(int i = 0; i < a; i++) {
				items.add((Item) in.readObject());
			}
		} catch(IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		return items;
	}

	/*private static void saveItems(List<Item> items){
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream("items.bin"))){
			
			out.writeInt(items.size());
			
			for(Item i : items) {
				out.writeUTF(i.getName());
				switch(i.getCategory()) {
					case VEGETABLE:
						out.writeChar('V');
						break;
					case DAIRY:
						out.writeChar('D');
						break;
					case CANDY:
						out.writeChar('C');
						break;
					case TOILETRY:
						out.writeChar('T');
						break;
					default:
						out.writeChar('X');
						break;
				}
				out.writeInt(i.getPrice());
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}*/

	private static void saveItems(List<Item> items) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("items.bin"))){
			out.writeInt(items.size());
			for(Item i : items) {
				out.writeObject(i);
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		List<Item> items = loadItems();
		
		printItems(items);

		Item.readFromInput(items);

		saveItems(items);
    
		printItems(items);
  }

	private static void printItems(List<Item> items){
		System.out.println("================================ Produkte ======================================");
		System.out.println("Produkte:");
		items.forEach(System.out::println);
		System.out.println("=============================== /Produkte ======================================");
	}
  
}
