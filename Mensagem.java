/**
  * Laboratorio 4  
  * Autor: Cristian Veggian e Bruno Keller
  * Ultima atualizacao: 09/05/2023
  */
import java.io.Serializable;

public class Mensagem implements Serializable {
    
	String mensagem;
	
    public Mensagem(String mensagem, String opcao){    	
        setMensagem(mensagem,opcao);
    }

    public Mensagem(String mensagem){
    	this.mensagem = new String(mensagem);
    }
    public String getMensagem(){
    	return this.mensagem;
    }
    public void setMensagem(String fortune, String opcao){
    	String mensagem="";
    	
    	switch(opcao){
    	case "\\read": {
        		
    		mensagem += "{\"method\":\"read\",\"args\":[\"\"]}";

			break;
		}
    	case "\\write": {
    		                		
        		mensagem +="{\"method\":\"write\",\"args\":[\""+ fortune +"\"]}";    
    			break;
    		}
    	}//fim switch
    	this.mensagem = mensagem;
    }
    
}