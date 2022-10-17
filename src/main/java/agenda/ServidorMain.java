package agenda;

import agenda.interfaces.ICliente;
import agenda.interfaces.IServidor;
import agenda.modelos.Alerta;
import agenda.modelos.Compromisso;
import agenda.modelos.Cliente;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import agenda.threads.NotificaClienteCompromissoThread;
import agenda.threads.NotificacaoThread;

public class ServidorMain extends UnicastRemoteObject implements IServidor{
	private ArrayList<Cliente> clientes = new ArrayList<>();
	public ArrayList<Alerta> alertas = new ArrayList<>();
	
	public static void main(String[] args) throws RemoteException, NotBoundException{
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
		IServidor referenciaServidor = new ServidorMain();
		referenciaServicoNomes.rebind("Agenda", referenciaServidor);
	}
	
	public ServidorMain() throws RemoteException{
		new NotificacaoThread(this);
	}

	@Override
	public void cadastrarCliente(String nome, ICliente referenciaCliente) throws RemoteException {
		if(retornaCliente(nome) != null){
			//throw new RemoteException("Cliente com esse nome já existe");
			return;
		}
		Cliente cliente = new Cliente(nome, referenciaCliente);
		clientes.add(cliente);
		System.out.println("Cadastrar Cliente: Cliente " + nome + " cadastrado");
	}
	
	public Cliente retornaCliente(String nome){
		for (Cliente cl: clientes){
			if(cl.nome.equals(nome)){
				return cl;
			}
		}
		return null;
	}

	@Override
	public void cadastrarCompromisso(String nomeCliente, Compromisso compromisso) throws RemoteException{
		Cliente cliente = retornaCliente(nomeCliente);
		if (cliente == null){
			//throw new RemoteException("Não existe cliente com esse nome");
			return;
		}
		cliente.addCompromisso(compromisso);
		for (String nome : compromisso.convidados){
			Cliente convidado = retornaCliente(nome);
			if(convidado != null){
				new NotificaClienteCompromissoThread(convidado.referenciaCliente, compromisso, true);
			}
			/*else{
				compromisso.convidados.remove(nome);
			}*/
		}
		System.out.println("Cadastrar compromisso: Compromisso " + compromisso.nome + " cadastrado para o cliente " + nomeCliente);
	}
	
	@Override
	public void cancelarCompromisso(String nomeCliente, String nomeCompromisso) throws RemoteException {
		Cliente cliente = retornaCliente(nomeCliente);
		Compromisso compromisso = null;
		boolean isDono = false;
		for(Compromisso cm : cliente.agenda){
			if(cm.nome.equals(nomeCompromisso)){
				compromisso = cm;
				isDono = cm.nomeCliente.equals(nomeCliente); 
				break;
			}
		}
		if(compromisso == null){
			//throw new RemoteException("Compromisso não existe");
			return;
		}
		cliente.agenda.remove(compromisso);
		cancelarAlerta(compromisso.nome, compromisso.nomeCliente);
		if(isDono){
			Cliente convidado = null;
			for(String cl : compromisso.convidados){
				cancelarAlerta(compromisso.nome, cl);
				convidado = retornaCliente(cl);
				if (convidado != null){
					convidado.agenda.remove(compromisso);
				}
			}
			System.out.println("Cancelar compromisso: Compromisso " + nomeCompromisso + " cancelado do cliente " + nomeCliente);
		}
	}

	@Override
	public void cadastrarCompromissoComAlerta(String nomeCliente, Compromisso compromisso, Calendar horarioAlerta) throws RemoteException {
		cadastrarCompromisso(nomeCliente, compromisso);
		Alerta alerta = new Alerta(nomeCliente, horarioAlerta, compromisso);
		alertas.add(alerta);
		System.out.println("Cadastrar compromisso com alerta: Alerta cadastrado do cliente " + nomeCliente);
	}

	@Override
	public void cancelarAlerta(String nomeCompromisso, String nomeCliente) throws RemoteException {
		Alerta alerta = null;
		for(Alerta al : alertas){
			if(al.nomeUsuario.equals(nomeCliente) && al.compromisso.nome.equals(nomeCompromisso)){
				alerta = al;
				break;
			}
		}
		if(alerta == null){
			//throw new RemoteException("O alerta não está cadastrado");
			return;
		}
		alertas.remove(alerta);
		System.out.println("Cancelar alerta: Alerta cancelado do compromisso " + nomeCompromisso + " do cliente " + nomeCliente);
	}

	@Override
	public ArrayList<Compromisso> consultaCompromissos(String nomeCliente, Calendar dataCompromisso) throws RemoteException {
		Cliente cliente = retornaCliente(nomeCliente);
		if(cliente == null){
			throw new RemoteException("O cliente não está cadastrado");
		}
		ArrayList<Compromisso> retorno = new ArrayList<>();
		for (Compromisso cm : cliente.agenda){
			if(cm.data.get(Calendar.YEAR) == dataCompromisso.get(Calendar.YEAR) && cm.data.get(Calendar.MONTH) == dataCompromisso.get(Calendar.MONTH) && cm.data.get(Calendar.DAY_OF_MONTH) == dataCompromisso.get(Calendar.DAY_OF_MONTH)){
				retorno.add(cm);
			}
		}
		System.out.println("Consulta de compromissos: Retorno dos compromisso do cliente " + nomeCliente);
		return retorno;
	}

	@Override
	public void respostaNotificacao(String nomeCliente, Compromisso compromisso) throws RemoteException {
		Cliente cliente = retornaCliente(nomeCliente);
		cliente.addCompromisso(compromisso);
		System.out.println("Cadastro compromisso de resposta de notificação: Compromisso do cliente " + nomeCliente);
	}

	@Override
	public void respostaNotificacaoComAlerta(String nomeCliente, Compromisso compromisso, Calendar horarioAlerta) throws RemoteException {
		respostaNotificacao(nomeCliente, compromisso);
		Alerta alerta = new Alerta(nomeCliente, horarioAlerta, compromisso);
		alertas.add(alerta);
		System.out.println("Cadastro compromisso de resposta de notificação com alarme: Compromisso do cliente " + nomeCliente);
	}
}
