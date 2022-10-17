package agenda.modelos;

import java.util.ArrayList;
import agenda.interfaces.ICliente;

public class Cliente {
	public String nome;
	public ICliente referenciaCliente;
	public ArrayList<Compromisso> agenda;
	
	public Cliente(String nome, ICliente referenciaCliente){
		this.nome = nome;
		this.referenciaCliente = referenciaCliente;
		this.agenda = new ArrayList<>();
	}
	
	public void addCompromisso(Compromisso compromisso){
		agenda.add(compromisso);
	}
}
