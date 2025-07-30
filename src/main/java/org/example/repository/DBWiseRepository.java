package org.example.repository;

import org.example.db.util.DBConnectionUtil;
import org.example.entity.Wise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
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
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = getConnection();
            pstm = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, author);
            pstm.setString(2, content);
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, null);
        }

        return -1;
    }

    @Override
    public List<Wise> findAll() {
        List<Wise> wiseList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstm = con.prepareStatement(FIND_ALL_QUERY);
            rs = pstm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String content = rs.getString("content");

                Wise wise = new Wise(id, author, content);
                wiseList.add(wise);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, rs);
        }

        wiseList.sort((origin, other) -> Integer.compare(other.getId(), origin.getId()));
        return Collections.unmodifiableList(wiseList);
    }

    @Override
    public void remove(int targetId) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = getConnection();
            pstm = con.prepareStatement(DELETE_BY_ID_QUERY);
            pstm.setInt(1, targetId);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, null);
        }
    }

    @Override
    public List<Wise> findByType(String type, String keyword) {
        List<Wise> wiseList = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            String sql = FIND_BY_TYPE_QUERY_PREFIX;
            if ("author".equalsIgnoreCase(type)) {
                sql += "author LIKE ?";
            } else if ("content".equalsIgnoreCase(type)) {
                sql += "content LIKE ?";
            }

            con = getConnection();
            pstm = con.prepareStatement(sql);
            pstm.setString(1, "%" + keyword + "%");
            rs = pstm.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String content = rs.getString("content");

                Wise wise = new Wise(id, author, content);
                wiseList.add(wise);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, rs);
        }

        return wiseList;
    }

    @Override
    public void modify(int id, String newContent, String newAuthor) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstm = con.prepareStatement(UPDATE_QUERY);

            pstm.setString(1, newContent);
            pstm.setString(2, newAuthor);
            pstm.setInt(3, id);

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, rs);
        }
    }

    @Override
    public void clear() {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = getConnection();
            pstm = con.prepareStatement(DELETE_ALL_QUERY);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstm, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
