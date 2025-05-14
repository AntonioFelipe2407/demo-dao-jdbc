package application;

import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;

public class Program {
    public static void main(String[] args) {

        //test
        Department obj = new Department(1,"book");
        Seller seller = new Seller(21, "bob", "bob@gmail.com", LocalDate.now(), 400.40, obj );

        System.out.println(seller);

    }
}
