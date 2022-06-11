package de.hs_kl.staab.playground;

public class SimpleExampleClass {

	String a;
	int b;

	public SimpleExampleClass(String a, int b) {
		super();
		this.a = a;
		this.b = b;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "SimpleExampleClass [a=" + a + ", b=" + b + "]";
	}

}
