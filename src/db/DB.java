package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

//Classe com métodos estáticos auxiliares

public class DB {

    // Objeto para estabelecer a conexão com o banco de dados
    private static Connection conn = null;

    // Função para conectar com o banco de dados
    public static Connection getConnection() {
        if(conn == null) {
            try {
                Properties props = loadProperties(); // Cria um objeto que recebe o objeto da função loadProperties
                String url = props.getProperty("dburl"); // url armazena a url
                conn = DriverManager.getConnection(url, props);
            }
            //Exceção lançada caso ocorra um erro na comunicação com o banco de dados
            catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn; // retorna a váriavel armazenando os valores para acessar o banco de dados
    }

    //Fecha a conexão com o banco de dados
    public static void closeConnection() {
        if (conn != null){
            try{
                conn.close();
            }
            catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }

    //Metodo para carregar as propriedades do arquivo db.properties
    private static Properties loadProperties() {
        try(FileInputStream fs = new FileInputStream("db.properties")){
            Properties props = new Properties();
            props.load(fs); // load faz a leitura e guarda os pares chave-valor do arquivo
            return props; //retorna o objeto do tipo Properties
        }
        catch (IOException e) {
            throw new DbException(e.getMessage()); // Lança a exception personalizada
        }
    }

    // metodo auxiliar responsável por fechar um obj do tipo Statement
    public static void closeStatement(Statement st){
        if(st != null){ // se o objeto estiver instanciado executa
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs){
        if(rs != null){ // se o objeto estiver instanciado executa
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

}
