package shelf;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Shelf<T extends ShelfItem> implements Iterable<T> {
	private T upperLeft;
	private T upperRight;
	private T lowerLeft;
	private T lowerRight;
	
	public Shelf() {
		
	}

	public T getUpperLeft() {
		return upperLeft;
	}

	public T getUpperRight() {
		return upperRight;
	}

	public T getLowerLeft() {
		return lowerLeft;
	}

	public T getLowerRight() {
		return lowerRight;
	}

	public void setUpperLeft(T upperLeft) {
		this.upperLeft = upperLeft;
	}

	public void setUpperRight(T upperRight) {
		this.upperRight = upperRight;
	}

	public void setLowerLeft(T lowerLeft) {
		this.lowerLeft = lowerLeft;
	}

	public void setLowerRight(T lowerRight) {
		this.lowerRight = lowerRight;
	}
	
	public T get(int index) {
		switch (index){
			case 0:
				return upperLeft;
			case 1:
				return upperRight;
			case 2:
				return lowerLeft;
			case 3:
				return lowerRight;
			default:
				throw new IllegalArgumentException();
			}
	}
	
	public void set(int index, T item) {
		switch (index){
	      	case 0:
	      		upperLeft = item;
	      		break;
	      	case 1:
	      		upperRight = item;
	      		break;
	      	case 2:
	      		lowerLeft = item;
	      		break;
	      	case 3:
	      		lowerRight = item;
	      		break;
	      	default:
	      		throw new IllegalArgumentException();
		}
	}
	
	public void takeFrom(Shelf<? extends T> other) {
		if(other == null)
			throw new IllegalArgumentException();
		
		for(int i = 0; i < 4; i++) {
			this.set(i, other.get(i));
			other.set(i, null);
		}
	}

	public T max(Comparator<T> comparator){
		if(comparator == null) 
			throw new IllegalArgumentException();
		
		T maxItem = null;
		
		for(int i = 0; i < 4; i++) {
			if(get(i) != null && (maxItem == null || comparator.compare(maxItem, get(i)) < 0))
				maxItem = get(i);
		}
		
		return maxItem;
	}
	
	public static <S extends ShelfItem>void transferAndTrim(Shelf<S> from, Shelf<? super S> to) {
		if(from == null || to == null)
			throw new IllegalArgumentException();
		
		for(int j = 0; j < 4; j++) {
			to.set(j, null);
		}
		
		int toIndex = 0;
		
		for(int fromIndex = 0; fromIndex < 4; fromIndex++) {
			if(from.get(fromIndex) != null) {
				to.set(toIndex++, from.get(fromIndex));
				from.set(fromIndex, null);
			}
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ShelfIterator();
	}
	
	private class ShelfIterator implements Iterator<T>{
		
		private int index;
		
		@Override
		public boolean hasNext() {
			return index < 4;
		}
		
		@Override
		public T next() {
			if(hasNext())
				return get(index++);
			else
				throw new NoSuchElementException();
		}
	}

}
