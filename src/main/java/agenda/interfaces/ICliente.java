package agenda.interfaces;

import agenda.modelos.Compromisso;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICliente extends Remote{
	void recebeNotificacaoNovoCompromisso(Compromisso compromisso) throws RemoteException;
	void recebeNotificacaoAlertaCompromisso(Compromisso compromisso, String texto) throws RemoteException;
}
