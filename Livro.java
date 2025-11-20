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
public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private String codigo; // código único (ex: L001)
    private int quantidade; // total de cópias
    private int disponiveis; // cópias disponíveis

    public Livro(int id, String titulo, String autor, String categoria, String codigo, int quantidade, int disponiveis) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.disponiveis = disponiveis;
    }

    // Construtor para novo cadastro (disponiveis = quantidade)
    public Livro(String titulo, String autor, String categoria, String codigo, int quantidade) {
        this(0, titulo, autor, categoria, codigo, quantidade, quantidade);
    }

    // Getters / Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getDisponiveis() {
        return disponiveis;
    }

    public void setDisponiveis(int disponiveis) {
        this.disponiveis = disponiveis;
    }

    // disponibilidade baseada em disponiveis > 0
    public boolean isDisponivel() {
        return disponiveis > 0;
    }

    // empresta uma cópia (retorna true se teve disponível)
    public boolean emprestarCopia() {
        if (disponiveis > 0) {
            disponiveis--;
            return true;
        }
        return false;
    }

    // devolve uma cópia (não ultrapassa quantidade)
    public boolean devolverCopia() {
        if (disponiveis < quantidade) {
            disponiveis++;
            return true;
        }
        return false;
    }
    
    // Representação textual (aparece no console).
    @Override
    public String toString() {
        return "Código Livro:" + codigo
                + " Titulo:" + titulo
                + " Autor=%s:" + autor
                + " Categoria:" + categoria
                + " Quantidade:" + quantidade
                + " Disponivel:" + disponiveis + "\n"; 
    }

    // CSV: id;titulo;autor;categoria;codigo;quantidade;disponiveis
    // Converte o livro para formato CSV.
    public String toCSV() {
        // evitar ';' nos campos para simplicidade; idealmente fazer escape em produção
        return String.format("%d;%s;%s;%s;%s;%d;%d",
                id,
                titulo == null ? "" : titulo,
                autor == null ? "" : autor,
                categoria == null ? "" : categoria,
                codigo == null ? "" : codigo,
                quantidade,
                disponiveis);
    }

    // fromCSV - tolerante a campos faltando
    // Constrói um livro a partir de uma linha de CSV.
    public static Livro fromCSV(String linha) {
        if (linha == null || linha.trim().isEmpty()) {
            return null;
        }
        String[] p = linha.split(";", -1);
        try {
            int id = Integer.parseInt(p[0]);
            String titulo = p.length > 1 ? p[1] : "";
            String autor = p.length > 2 ? p[2] : "";
            String categoria = p.length > 3 ? p[3] : "";
            String codigo = p.length > 4 ? p[4] : "";
            int quantidade = p.length > 5 && !p[5].isEmpty() ? Integer.parseInt(p[5]) : 1;
            int disponiveis = p.length > 6 && !p[6].isEmpty() ? Integer.parseInt(p[6]) : quantidade;
            Livro l = new Livro(id, titulo, autor, categoria, codigo, quantidade, disponiveis);
            // normalize codigo
            if (l.getCodigo() != null) {
                l.setCodigo(l.getCodigo().trim().toUpperCase());
            }
            return l;
        } catch (Exception e) {
            // linha mal formatada
            return null;
        }
    }

    /**
     * Dois livros são iguais se seus códigos forem iguais.
     */    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livro)) {
            return false;
        }
        Livro livro = (Livro) o;
        return Objects.equals(codigo, livro.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
