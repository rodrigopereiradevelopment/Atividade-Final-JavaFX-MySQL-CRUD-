package com.example.fxmysqlfxml;

import java.time.LocalDate;

public class Student {

    private Long id;
    private String name;
    private String email;
    // NOVOS CAMPOS DO REQUISITO:
    private LocalDate birthDate;
    private String phone;
    private Boolean active = true; // Valor padrão para evitar NullPointerException na inicialização

    // 1. Construtor Padrão (necessário para o DAO)
    public Student() {
    }

    // 2. Construtor com Campos (opcional, mas útil)
    public Student(Long id, String name, String email, LocalDate birthDate, String phone, Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
        this.active = active;
    }

    // GETTERS E SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
