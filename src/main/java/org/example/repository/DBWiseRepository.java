package org.example.repository;

import org.example.db.util.DBConnectionUtil;
import org.example.entity.Wise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBWiseRepository implements WiseRepository {

    private static final String INSERT_QUERY = "INSERT INTO wise(author, content) VALUES ( ?, ? )";
    private static final String FIND_ALL_QUERY = "SELECT * FROM wise";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM wise WHERE id = ?";
    private static final String DELETE_ALL_QUERY = "DELETE FROM wise";
    private static final String FIND_BY_TYPE_QUERY_PREFIX = "SELECT * FROM wise WHERE ";
    private static final String UPDATE_QUERY = "UPDATE wise SET content = ?, author = ? WHERE id = ?";

    @Override
    public int register(String author, String content) {
        try (Connection con = getConnection();
             PreparedStatement pstm = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, author);
            pstm.setString(2, content);
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    @Override
    public List<Wise> findAll() {
        List<Wise> wiseList = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pstm = con.prepareStatement(FIND_ALL_QUERY);
             ResultSet rs = pstm.executeQuery()) {

            fillWiseList(rs, wiseList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sortAndUnmodifiable(wiseList);
    }

    @Override
    public void remove(int targetId) {
        try (Connection con = getConnection();
             PreparedStatement pstm = con.prepareStatement(DELETE_BY_ID_QUERY)) {

            pstm.setInt(1, targetId);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Wise> findByType(String type, String keyword) {
        String sql = getDynamicQuery(type);
        List<Wise> wiseList = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pstm = prepareStatement(con, sql, "%" + keyword + "%");
             ResultSet rs = pstm.executeQuery()) {

            fillWiseList(rs, wiseList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sortAndUnmodifiable(wiseList);
    }

    @Override
    public void modify(int id, String newContent, String newAuthor) {
        try (Connection con = getConnection();
             PreparedStatement pstm = con.prepareStatement(UPDATE_QUERY)) {

            pstm.setString(1, newContent);
            pstm.setString(2, newAuthor);
            pstm.setInt(3, id);

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (Connection con = getConnection();
             PreparedStatement pstm = con.prepareStatement(DELETE_ALL_QUERY)) {

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fillWiseList(ResultSet rs, List<Wise> wiseList) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("id");
            String author = rs.getString("author");
            String content = rs.getString("content");

            Wise wise = new Wise(id, author, content);
            wiseList.add(wise);
        }
    }

    private static String getDynamicQuery(String type) {
        String sql = FIND_BY_TYPE_QUERY_PREFIX;
        if ("author".equalsIgnoreCase(type)) {
            sql += "author LIKE ?";
        } else if ("content".equalsIgnoreCase(type)) {
            sql += "content LIKE ?";
        }
        return sql;
    }

    private PreparedStatement prepareStatement(Connection con, String sql, String parameter) throws SQLException {
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, parameter);
        return pstm;
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}