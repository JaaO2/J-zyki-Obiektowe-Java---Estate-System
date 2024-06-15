package estateSystem;

public class menuItem {
	private final int key;
	private final String name;
	private final String function;
	private final int role;

	menuItem(int key, String name, String function, int role) {
		this.key = key;
		this.name = name;
		this.function = function;
		this.role = role;
	}

	public int getKey() {
		return key;
	}

	public String getFunction () {
		return function;
	}
	public String getItem () {
		return this.key + ". " + this.name;
	}
	public int getRole () { return this.role; }
}