package simuladorsicxe;

public class Endereco {

    private String valor;
    private int endereco ;

    public Endereco(int endereco, String valor) {
        this.endereco = endereco;
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getEndereco(){
        return  endereco;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }
}
