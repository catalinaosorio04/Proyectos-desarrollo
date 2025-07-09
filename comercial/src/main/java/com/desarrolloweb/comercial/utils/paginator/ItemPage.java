package com.desarrolloweb.comercial.utils.paginator;

public class ItemPage {
	private int numero;
	private boolean actual;
	
	public ItemPage(int numero, boolean actual) {
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}
}
