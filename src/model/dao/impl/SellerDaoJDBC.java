package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    //Esqueleto da implementação
    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null; // Será usado para preparar e executar a consulta SQL
        ResultSet rs = null; // Armazena o resultado da consulta
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            st.setInt(1, id); // passa o id para a consulta
            rs = st.executeQuery(); // executa a consulta

            // Verifica se a consulta retornou algum registro
            if (rs.next()) {
                Department dep = instantiateDepartment(rs); // Intancia o objeto dep
                Seller obj = instantiateSeller(rs, dep); // Instancia o objeto obj
                return obj;
            }
            return null; // se pular o if, retorna null
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException { // propagando a exception

        Department dep = new Department();
        // Pegando os valores dos campos do Department
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException { // propagando a exception

        Seller obj = new Seller();
        // Pegando os valores dos campos do Seller e associando com Department
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
        obj.setDepartment(dep);
        return obj;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
