package agenda.threads;

import java.util.Calendar;
import agenda.ServidorMain;
import agenda.interfaces.IServidor;
import agenda.modelos.Alerta;

public class NotificacaoThread extends Thread{
	private IServidor referenciaServidor;
	
	public NotificacaoThread(IServidor referenciaServidor) {
		this.referenciaServidor = referenciaServidor;
		this.start();
	}

	public void run() {
		try{
			Calendar dataAtual = null;
			while(true){
				dataAtual = Calendar.getInstance();
				System.out.println("Ciclo de alertas: " + dataAtual.getTime());
				var alertas = ((ServidorMain) referenciaServidor).alertas;
				for(Alerta al : alertas){
					var horarioAlerta = al.horarioAlerta;
					if(horarioAlerta.get(Calendar.YEAR) == dataAtual.get(Calendar.YEAR) && 
							horarioAlerta.get(Calendar.MONTH) == dataAtual.get(Calendar.MONTH) && 
							horarioAlerta.get(Calendar.DAY_OF_MONTH) == dataAtual.get(Calendar.DAY_OF_MONTH) &&
							horarioAlerta.get(Calendar.HOUR_OF_DAY) == dataAtual.get(Calendar.HOUR_OF_DAY) &&
							horarioAlerta.get(Calendar.MINUTE) == dataAtual.get(Calendar.MINUTE)){
						var referenciaCliente = ((ServidorMain) referenciaServidor).retornaCliente(al.nomeUsuario).referenciaCliente;
						new NotificaClienteCompromissoThread(referenciaCliente, al.compromisso, false);
						((ServidorMain) referenciaServidor).cancelarAlerta(al.compromisso.nome, al.nomeUsuario);
					}
				}
				Thread.sleep(60000);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
