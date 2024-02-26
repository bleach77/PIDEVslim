package org.example.services;

import org.example.entites.Entrepot;
import org.example.entites.StatuE;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntrepotService implements IService<Entrepot> {

    private Connection connection;

    public EntrepotService() {
        connection = MyDatabase.getInstance().getConnection();
    }
/*
    @Override
    public void ajouter(Entrepot entrepot) throws SQLException {
        String sql = "insert into entrepot (NomE, AdresseE, CapaciteE, StatutE) " +
                "values('" + entrepot.getNomE() + "','" + entrepot.getAdresseE() + "'" +
                "," + entrepot.getCapaciteE() +
                "," + entrepot.getStatutE() + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
*/
//    public void ajouter(Entrepot entrepot) throws SQLException {
//        String sql = "INSERT INTO entrepot (NomE, AdresseE, CapaciteE, StatutE) VALUES (?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, entrepot.getNomE());
//        statement.setString(2, entrepot.getAdresseE());
//        statement.setInt(3, entrepot.getCapaciteE());
//        statement.setString(4, entrepot.getStatutE().name());
//        statement.executeUpdate();
//    }

    public void ajouter(Entrepot entrepot) throws SQLException {
        String sql = "INSERT INTO entrepot (NomE, AdresseE, CapaciteE, StatutE) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, entrepot.getNomE());
        statement.setString(2, entrepot.getAdresseE());
        statement.setInt(3, entrepot.getCapaciteE());
        statement.setString(4, entrepot.getStatutE().name()); // Insérer le nom de l'énumération
        statement.executeUpdate();
    }

    @Override
    public void modifier(Entrepot entrepot) throws SQLException {
        System.out.println("--->"+entrepot);
        String sql = "update entrepot set NomE = ?, AdresseE = ?, CapaciteE = ?, StatutE = ? where EntrepotID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, entrepot.getNomE());
        preparedStatement.setString(2, entrepot.getAdresseE());
        preparedStatement.setInt(3, entrepot.getCapaciteE());
        preparedStatement.setString(4, entrepot.getStatutE().name());
        preparedStatement.setInt(5, entrepot.getEntrepotID());
        preparedStatement.executeUpdate();
        System.out.println("YESSSSs");
    }

    @Override
    public void supprimer(int EntrepotID) throws SQLException {
        String sql = "delete from entrepot where EntrepotID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, EntrepotID);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Entrepot> recuperer() throws SQLException {
        String sql = "select * from entrepot";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Entrepot> entrepots = new ArrayList<>();
        while (rs.next()) {
            Entrepot e = new Entrepot();
            e.setEntrepotID (rs.getInt("EntrepotID"));
            e.setNomE(rs.getString("NomE"));
            e.setAdresseE(rs.getString("AdresseE"));
            e.setCapaciteE(rs.getInt("CapaciteE"));
            e.setStatutE(StatuE.valueOf(rs.getString("StatutE")));
            entrepots.add(e);
        }
        return entrepots;
    }




}