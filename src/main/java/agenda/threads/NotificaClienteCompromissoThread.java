package agenda.threads;

import java.util.Scanner;
import agenda.ClienteMain;
import agenda.interfaces.ICliente;
import agenda.modelos.Compromisso;

public class NotificaClienteCompromissoThread extends Thread{
	private ICliente referenciaConvidado;
	private Compromisso compromisso;
	private boolean isNovoCompromisso;
	
	public NotificaClienteCompromissoThread(ICliente referenciaConvidado, Compromisso compromisso, boolean isNovoCompromisso) {
		this.referenciaConvidado = referenciaConvidado;
		this.compromisso = compromisso;
		this.isNovoCompromisso = isNovoCompromisso;
		this.start();
	}

	public void run() {
		try{
			if(isNovoCompromisso){
				referenciaConvidado.recebeNotificacaoNovoCompromisso(compromisso);
			}else{
				referenciaConvidado.recebeNotificacaoAlertaCompromisso(compromisso, "Oi, você tem uma nova notificação");
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
