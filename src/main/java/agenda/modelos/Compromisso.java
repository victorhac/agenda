package agenda.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Compromisso implements Serializable{
	public String nomeCliente;
	public String nome;
	public Calendar data;
	public ArrayList<String> convidados;

	public Compromisso(String nome, String nomeCliente, Calendar data, ArrayList<String> convidados) {
		this.nomeCliente = nomeCliente;
		this.nome = nome;
		this.data = data;
		this.convidados = convidados;
	}
	
	public Compromisso(){}
}
