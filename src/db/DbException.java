package db;

// Criando uma exceção personalizada que não obriga

public class DbException extends RuntimeException{

  //Construtor que informa uma mensagem
  public DbException(String msg){
    super(msg);
  }
}
