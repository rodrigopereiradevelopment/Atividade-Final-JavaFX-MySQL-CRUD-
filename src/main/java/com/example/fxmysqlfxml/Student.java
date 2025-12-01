
package com.example.fxmysqlfxml;

public class Student {
    private Long id;
    private String nome;
    private String email;
    private String sala;
    private String modulo;
    private String curso;

    public Student(Long id, String nome, String email, String sala, String modulo, String curso) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.sala = sala;
        this.modulo = modulo;
        this.curso = curso;
    }

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
}
