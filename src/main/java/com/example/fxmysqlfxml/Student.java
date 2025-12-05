package com.example.fxmysqlfxml;

import java.time.LocalDate;

public class Student {
    private Long id;
    private String nome;
    private String email;

    // CAMPOS DO DB QUE ESTAVAM FALTANDO:
    private LocalDate birthDate; // Usando LocalDate para Data de Nascimento
    private String phone;
    private Boolean active; // Usando Boolean para Ativo

    // CAMPOS DO DB EXISTENTES:
    private String sala;
    private String modulo;
    private String curso;

    // CONSTRUTOR VAZIO (necessário para o DAO ao ler do ResultSet)
    public Student() {}

    // CONSTRUTOR COMPLETO
    public Student(Long id, String nome, String email, LocalDate birthDate, String phone, Boolean active, String sala, String modulo, String curso) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
        this.active = active;
        this.sala = sala;
        this.modulo = modulo;
        this.curso = curso;
    }

    // --- Getters e Setters EXISTENTES ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    // --- NOVOS Getters e Setters ---
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}