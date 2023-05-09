/**
  * Laboratorio 4  
  * Autor: Cristian Veggian e Bruno Keller
  * Ultima atualizacao: 09/05/2023
  */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ServidorImpl implements IMensagem{
    
	ArrayList<Peer> alocados;
	
    public ServidorImpl() {
          alocados = new ArrayList<>();
    }
    
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
        	System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
		}
        return resposta;
    }    
    
	public String parserJSON(String json) {
		
        int methodIndex = json.indexOf("method\":\"", 0) + 9;
        int methodEndIndex = json.indexOf("\"", methodIndex);
        String method = json.substring(methodIndex, methodEndIndex);

        int argsIndex = json.indexOf("args\":[\"", 0) + 8;
        int argsEndIndex = json.indexOf("\"", argsIndex);
        String args = json.substring(argsIndex, argsEndIndex);

        Principal fileHandler = new Principal();

        if(method.equals("read")){
		    return "{\"result\": \""+ fileHandler.read() +"\"}";
        } else if(method.equals("write")){
            fileHandler.write(args);
		    return "{\"result\": \""+ args +"\"}";
        } else {
		    return "{\"result\": \"Falha ao identificar operação\"}";
        }


	}
    public void iniciar(){

    try {
    		
			List<Peer> listaPeers = new ArrayList<Peer>(EnumSet.allOf(Peer.class));
			System.out.println(listaPeers.get(0));

			SecureRandom sr = new SecureRandom();
    		Peer peer = listaPeers.get(sr.nextInt(listaPeers.size()));

    		Registry servidorRegistro;
    		try {
    			servidorRegistro = LocateRegistry.createRegistry(1099);
    		} catch (java.rmi.server.ExportException e){ 
    			System.out.print("Registro jah iniciado. Usar o ativo.\n");
    		}
    		servidorRegistro = LocateRegistry.getRegistry(); 
    		String [] listaAlocados = servidorRegistro.list();
    		for(int i=0; i<listaAlocados.length;i++)
    			System.out.println(listaAlocados[i]+" ativo.");
    		
    		
    		
    		int tentativas=0;
    		boolean repetido = true;
    		boolean cheio = false;
    		while(repetido && !cheio){
    			repetido=false;    			
    			peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    			for(int i=0; i<listaAlocados.length && !repetido; i++){
    				
    				if(listaAlocados[i].equals(peer.getNome())){
    					System.out.println(peer.getNome() + " ativo. Tentando proximo...");
    					repetido=true;
    					tentativas=i+1;
    				}    			  
    			}
    			
    			if(listaAlocados.length>0 && tentativas==listaPeers.size()){ 
    				cheio=true;
    			}
    		}
    		
    		if(cheio){
    			System.out.println("Sistema cheio. Tente mais tarde.");
    			System.exit(1);
    		}
    		
            IMensagem skeleton  = (IMensagem) UnicastRemoteObject.exportObject(this, 0);
            servidorRegistro.rebind(peer.getNome(), skeleton);
            System.out.print(peer.getNome() +" Servidor RMI: Aguardando conexoes...");
                        
        } catch(Exception e) {
            e.printStackTrace();
        }        

    }
    
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }    
}
