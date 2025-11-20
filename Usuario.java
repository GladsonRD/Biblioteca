/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;
/**
 *
 * @author glads
 */
public class Usuario {

    private int id;
    private String nome;
    private String matricula;
    private String email;
    private String telefone;

    /*
      Construtor completo. Usado quando o ID já é conhecido (por exemplo, ao
      carregar do arquivo).
     */
    public Usuario(int id, String nome, String matricula, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.telefone = telefone;
    }

    /*
      Construtor para novo usuário (ID será atribuído pela classe Biblioteca).
     */
    public Usuario(String nome, String matricula, String email, String telefone) {
        this(0, nome, matricula, email, telefone);
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /*
      Representação simples do usuário, para exibição em console.
     */
    @Override
    public String toString() {
        return String.format("Usuário{id=%d, nome='%s', matrícula='%s', e-mail='%s', telefone='%s'}",
                id, nome, matricula, email, telefone);
    }

    /*
      Converte o usuário em uma linha CSV (delimitador ';'). Ordem:
      id;nome;matricula;email;telefone
     */
    public String toCSV() {
        return String.format("%d;%s;%s;%s;%s", id, nome, matricula, email, telefone);
    }

    /*
      Cria um objeto Usuario a partir de uma linha CSV. Retorna null se a linha
      estiver inválida.
     */
    public static Usuario fromCSV(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return null;
        }
        String[] partes = linha.split(";", -1); // mantém campos vazios
        if (partes.length < 5) {
            return null;
        }
        try {
            int id = Integer.parseInt(partes[0]);
            String nome = partes[1];
            String matricula = partes[2];
            String email = partes[3];
            String telefone = partes[4];
            return new Usuario(id, nome, matricula, email, telefone);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /*
      Dois usuários são considerados iguais se tiverem a mesma matrícula.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(matricula, usuario.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }
}
