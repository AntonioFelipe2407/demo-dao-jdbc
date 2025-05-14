package model.dao;

import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class DaoFactory {

    //Metodo estático responsável por instanciar o SellerDao
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(); // retorna a implementação do SellerDao
    }

}
