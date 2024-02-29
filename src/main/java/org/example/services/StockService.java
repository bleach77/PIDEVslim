package org.example.services;

import com.google.protobuf.Message;
import com.mysql.cj.Session;
import org.example.entites.Stock;
import org.example.utils.MyDatabase;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class StockService implements IService<Stock> {

    private Connection connection;

    public StockService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    /*
    @Override
    public void ajouter(Stock stock) throws SQLException {
        String sql = "insert into stock ((EntrepotID, PrixUnitaireS, CodeProduitS, QuantiteS, dateRestock) " +
                "values('" + stock.getEntrepotID() + "','" + stock.getPrixUnitaireS() + "'" +
                "," + stock.getCodeProduitS() +
                "," + stock.getQuantiteS() +
                "," + "NOW()"+ ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
     */

    public void ajouter(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (CodeProduitS, QuantiteS, PrixUnitaireS, dateRestockS) VALUES (?, ?, ?, NOW())";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, stock.getCodeProduitS());
        statement.setInt(2, stock.getQuantiteS());
        statement.setFloat(3, stock.getPrixUnitaireS());
        statement.executeUpdate();
    }


    @Override
    public void modifier(Stock stock) throws SQLException {
        String sql = "update stock set CodeProduitS = ?, QuantiteS = ?, PrixUnitaireS = ? where StockID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, stock.getCodeProduitS());
        preparedStatement.setInt(2, stock.getQuantiteS());
        preparedStatement.setFloat(3, stock.getPrixUnitaireS());
        preparedStatement.setInt(4, stock.getStockID());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(int StockID) throws SQLException {
        String sql = "delete from stock where StockID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, StockID);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Stock> recuperer() throws SQLException {
        String sql = "select * from stock";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Stock> stocks = new ArrayList<>();
        while (rs.next()) {
            Stock s = new Stock();
            s.setStockID (rs.getInt("StockID"));
            s.setCodeProduitS(rs.getInt("CodeProduitS"));
            s.setQuantiteS(rs.getInt("QuantiteS"));
            s.setPrixUnitaireS(rs.getFloat("PrixUnitaireS"));
            s.setdateRestockS(rs.getDate("dateRestockS"));

            stocks.add(s);
        }
        return stocks;
    }




}