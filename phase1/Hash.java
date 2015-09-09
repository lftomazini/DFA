public class Hash<E> {

    private HashObj[] table;

    public HashObj[] create(int size) {
	table = new HashObj[size];
	return table;
    }

    public HashObj[] add(HashObj element) {
	int index = element.key() % table.length;
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
	    if(table[i].eq(element)) return true;
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
	Hash t = new Hash();
	HashObj o1 = new HashObj(763, "beans");
	HashObj o2 = new HashObj(12938, "hello");
	HashObj o3 = new HashObj(28, "goodbye");
	t.create(5);
	t.add(o1);
	t.add(o2);
	t.add(o3);
	t.printHash();
    }
}