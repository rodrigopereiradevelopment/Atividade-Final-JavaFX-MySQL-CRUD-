
package com.example.fxmysqlfxml;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, nome, email, sala, modulo, curso FROM students ORDER BY id";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs.getLong("id"), rs.getString("nome"), rs.getString("email"), rs.getString("sala"), rs.getString("modulo"), rs.getString("curso")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Student insert(Student s) {
        String sql = "INSERT INTO students (nome, email, sala, modulo, curso) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getSala());
            ps.setString(4, s.getModulo());
            ps.setString(5, s.getCurso());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getLong(1));
            }
            return s;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Student s) {
        String sql = "UPDATE students SET nome=?, email=?, sala=?, modulo=?, curso=? WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getSala());
            ps.setString(4, s.getModulo());
            ps.setString(5, s.getCurso());
            ps.setLong(6, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
