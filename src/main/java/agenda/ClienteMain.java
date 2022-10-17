package agenda;

import agenda.threads.ClienteThread;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import agenda.interfaces.ICliente;
import agenda.interfaces.IServidor;
import agenda.modelos.Compromisso;

public class ClienteMain extends UnicastRemoteObject implements ICliente{
	private String nome;
	private IServidor referenciaServidor;
	private Scanner sc = new Scanner(System.in);
	private Scanner scInt = new Scanner(System.in);
	
	public static void main(String[] args) throws RemoteException, NotBoundException{
		Registry referenciaServicoNomes = LocateRegistry.getRegistry(1099);
		IServidor referenciaServidor = (IServidor) referenciaServicoNomes.lookup("Agenda");
		ICliente referenciaCliente = new ClienteMain(referenciaServidor);
	}
	
	public ClienteMain(IServidor referenciaServidor) throws RemoteException{
		System.out.println("Informe o nome do cliente: ");
		this.nome = sc.nextLine();
		referenciaServidor.cadastrarCliente(nome, this);
		this.referenciaServidor = referenciaServidor;
		ClienteThread clienteThread = new ClienteThread(this);
	}
	
	private Calendar retornaData(){
		int hora, minuto;
		
		Calendar data = retornaDataSemMinuto();
		
		System.out.println("Hora: ");
		hora = scInt.nextInt();
		System.out.println("Minuto:");
		minuto = scInt.nextInt();
		
		data.set(Calendar.HOUR_OF_DAY, hora);
		data.set(Calendar.MINUTE, minuto);
		return data;
	}
	
	private Calendar retornaDataSemMinuto(){
		int dia, mes, ano;
		
		System.out.println("Informe a data:");
		System.out.println("Dia: ");
		dia = scInt.nextInt();
		System.out.println("Mês: ");
		mes = scInt.nextInt();
		System.out.println("Ano: ");
		ano = scInt.nextInt();
		
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, dia);
		data.set(Calendar.MONTH, mes - 1);
		data.set(Calendar.YEAR, ano);
		return data;
	}
	
	private Compromisso retornaCompromisso(){
		System.out.println("Cadastro de Compromisso:");
		System.out.print("Informe o nome do compromisso: ");
		String nomeCompromisso = sc.nextLine();
		Calendar data = retornaData(); 
		ArrayList<String> convidados = new ArrayList<String>();
		int escolha;
		String nomeConvidado;
		do{
			System.out.println("Deseja informar um novo convidado? 1- sim, 2- não");
			escolha = scInt.nextInt();
			if(escolha != 1){
				break;
			}else{
				System.out.println("Informe o nome do convidado: ");
				nomeConvidado = sc.nextLine();
				convidados.add(nomeConvidado);
			}
		} while(true);
		return new Compromisso(nomeCompromisso, nome, data, convidados);
	}

	@Override
	public void recebeNotificacaoNovoCompromisso(Compromisso compromisso) throws RemoteException {
		/*System.out.println("Deseja participar do compromisso: 1- sim, 2- não");
		boolean aceitaParticipar = scInt.nextInt() == 1;
		if(aceitaParticipar){
			System.out.println("Deseja salvar um alerta para o compromisso: 1- sim, 2- não");
			boolean querAlerta = scInt.nextInt() == 1;
			if(querAlerta){
				Calendar data = retornaDataSemMinuto();
				referenciaServidor.respostaNotificacaoComAlerta(nome, compromisso, data);
			}else{*/
				referenciaServidor.respostaNotificacao(nome, compromisso);
			/*}
		}*/
	}

	@Override
	public void recebeNotificacaoAlertaCompromisso(Compromisso compromisso, String texto) throws RemoteException {
		System.out.println("Alerta para o compromisso \"" + compromisso.nome + "\". " + texto);
	}
	
	public void consultaCompromissos() throws RemoteException{
		Calendar data = retornaDataSemMinuto();
		ArrayList<Compromisso> compromissos = referenciaServidor.consultaCompromissos(nome, data);
		System.out.println("Compromissos:");
		if(compromissos.size() == 0){
			System.out.println("Não há compromissos cadastrados");
			return;
		}
		for (Compromisso cm : compromissos){
			System.out.println("Nome: " + cm.nome + ". Data: " + cm.data.getTime() + ". ");
			if(cm.convidados.size() > 0){
				System.out.println("Convidados: ");
				for (String nomeConvidado: cm.convidados){
					System.out.println(nomeConvidado);
				}
			}
		}
	}
	
	public void cadastrarCompromisso() throws RemoteException {
		Compromisso compromisso = retornaCompromisso();
		referenciaServidor.cadastrarCompromisso(nome, compromisso);
	}
	
	public void cadastrarCompromissoComAlerta() throws RemoteException {
		Compromisso compromisso = retornaCompromisso();
		System.out.println("Informe os dados sobre o alerta do compromisso: ");
		Calendar data = retornaData();
		referenciaServidor.cadastrarCompromissoComAlerta(nome, compromisso, data);
	}
	
	public void cancelarCompromisso() throws RemoteException {
		System.out.println("Informe o nome do compromisso: ");
		String nomeCompromisso = sc.nextLine();
		referenciaServidor.cancelarCompromisso(nome, nomeCompromisso);
	}
	
	public void cancelarAlerta() throws RemoteException {
		System.out.println("Informe o nome do compromisso do alerta que deseja cancelar: ");
		String nomeCompromisso = sc.next();
		referenciaServidor.cancelarAlerta(nomeCompromisso, nome);
	}
}
