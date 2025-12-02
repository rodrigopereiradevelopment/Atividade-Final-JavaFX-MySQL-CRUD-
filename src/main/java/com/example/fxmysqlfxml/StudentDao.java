package com.example.fxmysqlfxml;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    // Método auxiliar para mapear o ResultSet para o objeto Student
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();

        s.setId(rs.getLong("id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));

        // Novos campos: birth_date, phone, active
        Date sqlDate = rs.getDate("birth_date");
        if (sqlDate != null) {
            s.setBirthDate(sqlDate.toLocalDate()); // Converte java.sql.Date para LocalDate
        }
        s.setPhone(rs.getString("phone"));
        s.setActive(rs.getBoolean("active"));

        return s;
    }

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        // 1. Query ajustada para incluir os 3 novos campos
        String sql = "SELECT id, name, email, birth_date, phone, active FROM students ORDER BY name";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            // Lembre-se de adicionar um Alert para o usuário aqui, conforme boas práticas.
            throw new RuntimeException("Erro ao buscar alunos no DB: " + e.getMessage(), e);
        }
        return list;
    }

    public Student insert(Student s) {
        // 2. Query ajustada para incluir os 3 novos campos
        String sql = "INSERT INTO students (name, email, birth_date, phone, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());

            // 3. Definindo birth_date (LocalDate)
            if (s.getBirthDate() != null) {
                ps.setDate(3, Date.valueOf(s.getBirthDate()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            // 4. Definindo phone e active
            ps.setString(4, s.getPhone());
            ps.setBoolean(5, s.getActive());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getLong(1));
            }
            return s;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluno no DB: " + e.getMessage(), e);
        }
    }

    public void update(Student s) {
        // 5. Query ajustada para incluir os 3 novos campos no SET
        String sql = "UPDATE students SET name=?, email=?, birth_date=?, phone=?, active=? WHERE id=?";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());

            // 6. Definindo birth_date (LocalDate)
            if (s.getBirthDate() != null) {
                ps.setDate(3, Date.valueOf(s.getBirthDate()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            // 7. Definindo phone e active
            ps.setString(4, s.getPhone());
            ps.setBoolean(5, s.getActive());

            ps.setLong(6, s.getId()); // O ID fica no WHERE

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno no DB: " + e.getMessage(), e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar aluno no DB: " + e.getMessage(), e);
        }
    }

    // O método findById() também precisa ser atualizado, seguindo a lógica do findAll()
    // public Student findById(Long id) { ... }
}