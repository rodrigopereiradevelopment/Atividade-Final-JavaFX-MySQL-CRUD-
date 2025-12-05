package com.example.fxmysqlfxml;

import java.sql.*;
import java.time.LocalDate; // Import necessário para trabalhar com Date/LocalDate
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        // CORRIGIDO: Inclui TODAS as 9 colunas do DB
        String sql = "SELECT id, name, email, birth_date, phone, active, sala, modulo, curso FROM students";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getLong("id"));

                // CORRIGIDO: Usa o nome da coluna do DB ('name') e atribui ao campo 'nome' do objeto Java
                s.setNome(rs.getString("name"));

                s.setEmail(rs.getString("email"));

                // CORRIGIDO: Lê os campos adicionais (birth_date, phone, active)
                Date sqlDate = rs.getDate("birth_date");
                if (sqlDate != null) {
                    s.setBirthDate(sqlDate.toLocalDate());
                } else {
                    s.setBirthDate(null); // Trata datas nulas
                }
                s.setPhone(rs.getString("phone"));
                s.setActive(rs.getBoolean("active"));

                s.setSala(rs.getString("sala"));
                s.setModulo(rs.getString("modulo"));
                s.setCurso(rs.getString("curso"));

                list.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Student insert(Student s) {
        // CORRIGIDO: Inclui TODAS as 8 colunas que aceitam valor (exceto ID)
        String sql = "INSERT INTO students (name, email, birth_date, phone, active, sala, modulo, curso) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getNome());
            ps.setString(2, s.getEmail());

            // CORRIGIDO: Mapeamento dos novos campos (birth_date, phone, active)
            LocalDate birthDate = s.getBirthDate();
            if (birthDate != null) {
                ps.setDate(3, Date.valueOf(birthDate));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, s.getPhone());
            ps.setBoolean(5, s.getActive());

            // Mapeamento dos campos existentes
            ps.setString(6, s.getSala());
            ps.setString(7, s.getModulo());
            ps.setString(8, s.getCurso());

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
        // CORRIGIDO: Inclui TODAS as 8 colunas no SET
        String sql = "UPDATE students SET name=?, email=?, birth_date=?, phone=?, active=?, sala=?, modulo=?, curso=? WHERE id=?";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getNome());
            ps.setString(2, s.getEmail());

            // CORRIGIDO: Mapeamento dos novos campos
            LocalDate birthDate = s.getBirthDate();
            if (birthDate != null) {
                ps.setDate(3, Date.valueOf(birthDate));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, s.getPhone());
            ps.setBoolean(5, s.getActive());

            // Mapeamento dos campos existentes
            ps.setString(6, s.getSala());
            ps.setString(7, s.getModulo());
            ps.setString(8, s.getCurso());

            // ID é o último parâmetro
            ps.setLong(9, s.getId());
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