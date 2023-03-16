package entities;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	
	private List<Bloco> blocos = new ArrayList<>();

	public Memoria() {
	}
	
	public List<Bloco> getBlocos() {
		return blocos;
	}
	
	public void addBloco(Bloco bloco) {
		blocos.add(bloco);
	}
	
	public void removeBloco(Bloco bloco) {
		blocos.remove(bloco);
	}

}
