/**
  * Laboratorio 4  
  * Autor: Cristian Veggian e Bruno Keller
  * Ultima atualizacao: 09/05/2023
  */
public enum Peer {
    
    PEER1 {
        @Override
        public String getNome() {
            return "PEER1";
        }        
    },
    PEER2 {
        public String getNome() {
            return "PEER2";
        }        
    },
    PEER3 {
        public String getNome() {
            return "PEER3";
        }        
    };
    public String getNome(){
        return "NULO";
    }    
}
