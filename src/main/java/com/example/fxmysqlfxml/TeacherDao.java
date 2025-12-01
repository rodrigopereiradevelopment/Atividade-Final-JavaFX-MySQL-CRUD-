
package com.example.fxmysqlfxml;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao {

    public List<Teacher> findAll() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT id, nome, materia, periodo FROM teacher ORDER BY id";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Teacher(rs.getLong("id"), rs.getString("nome"), rs.getString("materia"), rs.getString("periodo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Teacher insert(Teacher t) {
        String sql = "INSERT INTO teacher (nome, materia, periodo) VALUES (?, ?, ?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getMateria());
            ps.setString(3, t.getPeriodo());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getLong(1));
            }
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Teacher t) {
        String sql = "UPDATE teacher SET nome=?, materia=?, periodo=? WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getMateria());
            ps.setString(3, t.getPeriodo());
            ps.setLong(4, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM teacher WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
