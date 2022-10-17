package agenda.interfaces;

import agenda.modelos.Compromisso;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

public interface IServidor extends Remote{
	void cadastrarCliente(String nomeCliente, ICliente referenciaCliente) throws RemoteException;
	void cadastrarCompromisso(String nomeCliente, Compromisso compromisso) throws RemoteException;
	void cadastrarCompromissoComAlerta(String nomeCliente, Compromisso compromisso, Calendar horarioAlerta) throws RemoteException;
	void cancelarCompromisso(String nomeCliente, String nomeCompromisso) throws RemoteException;
	void cancelarAlerta(String nomeCompromisso, String nomeCliente) throws RemoteException;
	ArrayList<Compromisso> consultaCompromissos(String nomeCliente, Calendar dataCompromisso) throws RemoteException;
	void respostaNotificacao(String nomeCliente, Compromisso compromisso) throws RemoteException;
	void respostaNotificacaoComAlerta(String nomeCliente, Compromisso compromisso, Calendar horarioAlerta) throws RemoteException;
}
