public class Hash<E> {

    private HashObj[] table;

    public HashObj[] create(int size) {
	table = new HashObj[size];
	return table;
    }

    public HashObj[] add(HashObj element) {
	int index = element.key() % this.table.length;
	if(table[index] == null) table[index] = element;
	else {
	    int originalIndex = index;
	    while(table[index] != null) {
		index = (index + 1) % table.length;
		if(index == originalIndex) {
		    System.out.println("table full");
		    return table;
		}
	    }
	    table[index] = element;
	}
	return table;
    }

    public boolean mem(HashObj element) {
	for(int i = 0; i < table.length; i++) {
	    if(table[i] != null) {
		if(table[i].eq(element)) return true;
	    }
	}
	return false;
    }

    public void printHash() {
	for(int i = 0; i < table.length; i++) {
	    if(table[i] == null) System.out.println("null");
	    else System.out.println(table[i].value());
	}
    }

    public static void main(String[] args) {
	// Example 1 of A
	Hash stringTable = new Hash();
	HashObj o1 = new HashObj(763, "beans");
	HashObj o2 = new HashObj(12938, "hello");
	HashObj o3 = new HashObj(28, "goodbye"); // collision
	HashObj o4 = new HashObj(123, "yoyo");
	stringTable.create(5);
	stringTable.add(o1);
	stringTable.add(o2);
	stringTable.add(o3);
        System.out.println(stringTable.mem(o4));
	System.out.println(stringTable.mem(o3));
	stringTable.printHash();
	System.out.println();

	// Example 2 of A
	Hash intTable = new Hash();
	HashObj i1 = new HashObj(3,3);
	HashObj i2 = new HashObj(21,21);
	HashObj i3 = new HashObj(156,156);
	HashObj i4 = new HashObj(13,13); // collision
	intTable.create(5);
	intTable.add(i1);
	intTable.add(i2);
	intTable.add(i4);
	System.out.println(intTable.mem(i3));
	System.out.println(intTable.mem(i2));
	intTable.printHash();
    }
}