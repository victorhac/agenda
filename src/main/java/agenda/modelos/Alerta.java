package agenda.modelos;

import java.util.Calendar;

public class Alerta{
	public String nomeUsuario;
	public Calendar horarioAlerta;
	public Compromisso compromisso;

	public Alerta(String nomeUsuario, Calendar horarioAlerta, Compromisso compromisso) {
		this.nomeUsuario = nomeUsuario;
		this.horarioAlerta = horarioAlerta;
		this.compromisso = compromisso;
	}
}
