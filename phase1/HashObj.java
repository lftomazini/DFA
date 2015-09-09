public class HashObj {

    private int key;
    private Object value;

    public HashObj(int k, Object value) {
	this.key = k;
	this.value = value;
    }

    public int key() { return this.key; }

    public Object value() { return this.value; }

    public boolean eq(HashObj other) { return this.key == other.key(); }
}