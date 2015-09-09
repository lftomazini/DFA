public class Test<E> {
    private E[] a;

    public Test(int size) {
	System.out.println(size);
	a = new E[size];
    }

    public void addElement(E element) {
	a[1] = element;
    }

    public static void main(String[] args) {
	Test b = new Test(3);
	b.addElement(4);
    }
}