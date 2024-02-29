package org.example.test;

import org.example.entites.Entrepot;
import org.example.entites.Stock;
import org.example.entites.StatuE;
import org.example.services.EntrepotService;
import org.example.services.Sendmail;
import org.example.services.StockService;
import org.example.utils.MyDatabase;
import org.example.entites.StatuE;


import java.sql.SQLException;
import java.util.Date;

import static org.example.entites.StatuE.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {


//        System.out.println(MyDataBase.getInstance());
//        System.out.println(MyDataBase.getInstance());
        Sendmail sender = new Sendmail();

        // Sample email details
        String toEmail = "slim-fady.hanafi@esprit.tn";
        String subject = "Test Email";
        String body = "This is a test email sent from JavaMail.";

        // Sending the email
        sender.envoyer(toEmail, subject, body);

        EntrepotService es = new EntrepotService();
        StockService ss = new StockService();


//        try {
//            ss.ajouter(new Stock(7,7,7));
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            String dateString = "2024-02-30";
//            ss.modifier(new Stock(2,77,77,77,java.sql.Date.valueOf(dateString)));
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            ss.supprimer(1);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        try {
//            System.out.println(ss.recuperer());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

/*
        try {
            es.ajouter(new Entrepot(2,"Dammarie","les-lys",7, IInactif));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

*/
        try {
            es.modifier(new Entrepot(43,"Dammarie","les-lys",77, Actif));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

/*
        try {
            es.supprimer(6);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        try {
            System.out.println(es.recuperer());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

 */


    }
}