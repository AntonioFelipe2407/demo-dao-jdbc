package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class DaoFactory {

    //Metodo estático responsável por instanciar o SellerDao
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection()); // retorna a implementação do SellerDao
    }

}
