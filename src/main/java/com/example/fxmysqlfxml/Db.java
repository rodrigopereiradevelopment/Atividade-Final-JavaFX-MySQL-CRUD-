
package com.example.fxmysqlfxml;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Db {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Db.class.getResourceAsStream("/com/example/fxmysqlfxml/config.properties")) {
            if (in == null) throw new RuntimeException("config.properties não encontrado");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config: " + e.getMessage(), e);
        }

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "nome VARCHAR(120) NOT NULL," +
                    "email VARCHAR(180) NOT NULL UNIQUE,"+
                    "sala VARCHAR(10) NOT NULL,"+
                    "modulo VARCHAR(5) NOT NULL,"+
                    "curso VARCHAR(120) NOT NULL)");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS teacher (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "nome VARCHAR(120) NOT NULL, " +
                            "materia VARCHAR(180) NOT NULL UNIQUE, " +
                            "periodo VARCHAR(120) NOT NULL)");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}
