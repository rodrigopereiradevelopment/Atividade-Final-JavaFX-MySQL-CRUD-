
package com.example.fxmysqlfxml;

public class Teacher
{
    private Long id;
    private String nome;
    private String materia;
    private String periodo;
    public Teacher(Long id, String nome, String materia, String periodo) {
        this.id = id;
        this.nome = nome;
        this.materia = materia;
        this.periodo = periodo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }


}
