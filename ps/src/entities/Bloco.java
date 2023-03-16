package entities;

public class Bloco {
	
	private int posicao;
	private String conteudo;
	
	public Bloco(int posicao, String conteudo) {
		this.posicao = posicao;
		this.conteudo = conteudo;
	}
	
	public int getPosicao() {
		return this.posicao;
	}
	
	public String getConteudo() {
		return this.conteudo;
	}

}
