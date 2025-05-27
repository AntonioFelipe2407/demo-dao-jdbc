package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //test
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("==== TEST 1: seller findById ====");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n==== TEST 2 seller findByDepartment ====");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n==== TEST 3 seller findAll ====");
        list = sellerDao.findAll();
        for (Seller obj : list){
            System.out.println(obj);
        }

        System.out.println("\n==== TEST 4 seller insert ====");
        LocalDate birthDate = LocalDate.of(1993, 10, 11);
        Seller newSeller = new Seller(null, "greg", "greg@gmail.com", Date.valueOf(birthDate).toLocalDate(), 4000.0, department);
        sellerDao.insert(newSeller); // insere no banco de dados o vendedor instanciado
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("\n==== TEST 5 seller update ====");
        seller = sellerDao.findById(1);
        seller.setName("Martha Waine");
        sellerDao.update(seller); // atualiza o nome do vendedor
        System.out.println("Update Completed!");

        System.out.println("\n==== TEST 6 seller delete ====");
        System.out.println("Enter id for delete teste: ");
        int sellerId = sc.nextInt();
        sellerDao.deleteById(sellerId);


        sc.close();
    }
}
