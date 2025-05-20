package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    //Esqueleto da implementação
    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        try {
            // Comando SQL
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                            + "VALUES "
                            + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS); // Retorna o id do novo vendedor inserido

            // Substituindo os place holders
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            // Executa o comando sql e retorna a quantidade de linhas afetadas
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys(); // Obtém o id do novo vendedor inserido
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            // Lança uma exception caso nenhuma linha seja alterada
            else {
                throw new DbException("Unexpected error! No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try {
            // Comando SQL
            st = conn.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?");

            //Substitui os place holders
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            // Executa o comando SQL
            st.executeUpdate();

        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
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
                Seller obj = instantiateSeller(rs, dep); // Instancia o objeto Seller
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
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null; // Será usado para preparar e executar a consulta SQL
        ResultSet rs = null; // Armazena o resultado da consulta
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");

            st.setInt(1, department.getId()); // passa o id para a consulta
            rs = st.executeQuery(); // executa a consulta

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            //Percorre o objeto do tipo Resultset enquanto ouver um proximo elemento
            while (rs.next()) {

                // objeto dep recebe o id do departamento que estiver dentro de rs
                Department dep = map.get(rs.getInt("DepartmentId"));

                // Se o id ainda não existir, é instanciado esse novo id e é guardado no map
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep); // Instancia o objeto obj
                list.add(obj);

            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

}
