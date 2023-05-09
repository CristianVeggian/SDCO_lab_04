/**
  * Laboratorio 4  
  * Autor: Cristian Veggian e Bruno Keller
  * Ultima atualizacao: 09/05/2023
  */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;


public class ClienteRMI {
    
	public static void read(IMensagem stub) {
        Mensagem mensagem = new Mensagem("", "1");
        Mensagem resposta;
        try {
            resposta = stub.enviar(mensagem);
            System.out.println(resposta.getMensagem());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void write(String fortune, IMensagem stub) {
        Mensagem mensagem = new Mensagem(fortune, "2");
        try {
            Mensagem resposta = stub.enviar(mensagem);
            System.out.println(resposta.getMensagem());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
                
		List<Peer> listaPeers = new ArrayList<Peer>(EnumSet.allOf(Peer.class));

		System.out.println(listaPeers.get(0));
		
        try {
                        
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);

            SecureRandom sr = new SecureRandom();
    		
            IMensagem stub = null;
            Peer peer = null;
            
    		boolean conectou=false;
    		while(!conectou){
    			peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    			try{    				
    				stub = (IMensagem) registro.lookup(peer.getNome());
    				conectou=true;
    			} catch(java.rmi.ConnectException e){
    				System.out.println(peer.getNome() + " indisponivel. ConnectException. Tentanto o proximo...");
    			} catch(java.rmi.NotBoundException e){
    				System.out.println(peer.getNome() + " indisponivel. NotBoundException. Tentanto o proximo...");
    			}
    		}
            System.out.println("Conectado no peer: " + peer.getNome());            
    		
    		
            String opcao="";
            Scanner leitura = new Scanner(System.in);
            do {
            	System.out.println("\\read");
            	System.out.println("\\write");
            	System.out.println("\\quit");
            	System.out.print(">> ");
            	opcao = leitura.next();
            	switch(opcao){
            	case "\\read": {
            		Mensagem mensagem = new Mensagem("", opcao);
            		Mensagem resposta = stub.enviar(mensagem);
            		System.out.println(resposta.getMensagem());
            		break;
            	}
            	case "\\write": {
            		System.out.print("Add fortune: ");
            		String fortune = leitura.nextLine();
            		fortune = leitura.nextLine();
            		Mensagem mensagem = new Mensagem(fortune, opcao);
            		Mensagem resposta = stub.enviar(mensagem);
            		System.out.println(resposta.getMensagem());
            		break;
            	}
            	}
            } while(opcao != "\\quit");
                        
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
