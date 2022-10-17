package agenda.threads;

import java.util.Scanner;
import agenda.ClienteMain;

public class ClienteThread extends Thread{
	private ClienteMain cliente;
	private Scanner sc = new Scanner(System.in);
	
	public ClienteThread(ClienteMain cliente) {
		this.cliente = cliente;
		this.start();
	}
	
	public void printMenu(){
		System.out.println("1- Cadastrar compromisso");
		System.out.println("2- Cadastrar compromisso com alerta");
		System.out.println("3- Cancelar compromisso");
		System.out.println("4- Cancelar alerta");
		System.out.println("5- Consultar compromissos");
		System.out.println("6- Sair");
	}

	public void run() {
		try {
			while(true){
				printMenu();
				int escolha = Integer.parseInt(sc.nextLine());
				if (escolha < 1 || escolha > 5){
					return;
				}
				switch(escolha){
					case 1:
						cliente.cadastrarCompromisso();
						break;
					case 2:
						cliente.cadastrarCompromissoComAlerta();
						break;
					case 3:
						cliente.cancelarCompromisso();
						break;
					case 4:
						cliente.cancelarAlerta();
						break;
					case 5:
						cliente.consultaCompromissos();
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("Exceção:" + e.getMessage());
		}
  }
}
