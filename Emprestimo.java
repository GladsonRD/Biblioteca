/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author glads
 */
public class Emprestimo {

    private int id;
    private Livro livro;
    private Usuario usuario;
    private int idLivro;
    private int idUsuario;

    private String codigoLivro;
    private String matriculaUsuario;

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevista;
    private LocalDate dataDevolucao;
    private double multa;

    public static final int PRAZO_DIAS = 7;
    public static final double VALOR_DIA_ATRASO = 1.50;

    // Construtor usado ao criar novo empréstimo
    public Emprestimo(Livro livro, Usuario usuario) {
        this.livro = livro;
        this.usuario = usuario;
        this.codigoLivro = livro.getCodigo();
        this.matriculaUsuario = usuario.getMatricula();
        this.idLivro = livro.getId();
        this.idUsuario = usuario.getId();
        this.dataEmprestimo = LocalDate.now();
        this.dataPrevista = LocalDate.now().plusDays(PRAZO_DIAS);
        this.dataDevolucao = null;
        this.multa = 0.0;
        this.idLivro = 0;
        this.idUsuario = 0;
    }

    // Construtor usado ao carregar do CSV
    public Emprestimo(int id, String codigoLivro, String matriculaUsuario,
            LocalDate dataEmprestimo, LocalDate dataPrevista,
            LocalDate dataDevolucao, double multa) {

        this.id = id;
        this.codigoLivro = codigoLivro;
        this.matriculaUsuario = matriculaUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataPrevista;
        this.dataDevolucao = dataDevolucao;
        this.multa = multa;
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCodigoLivro() {
        return codigoLivro;
    }

    public String getMatriculaUsuario() {
        return matriculaUsuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Empréstimo em aberto?
    public boolean emAberto() {
        return dataDevolucao == null;
    }

    // Registrar devolução
    public void registrarDevolucao(LocalDate dataDev) {
        this.dataDevolucao = dataDev;
        calcularMulta();
    }

    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String dataEmprestimoFmt() {
        return dataEmprestimo.format(FMT);
    }

    public String dataPrevistaFmt() {
        return dataPrevista.format(FMT);
    }

    public String dataDevolucaoFmt() {
        return dataDevolucao == null ? "PENDENTE" : dataDevolucao.format(FMT);
    }

    public long diasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataPrevista);
    }

    public String situacaoPrazo() {
        long dias = diasRestantes();

        if (dias > 0) {
            return "Faltam " + dias + " dia(s) para devolução.";
        } else if (dias == 0) {
            return "⚠️ A devolução é HOJE!";
        } else {
            return "❌ ATRASADO em " + Math.abs(dias) + " dia(s)!";
        }
    }

    // Calcular multa
    public double calcularMulta() {
        LocalDate fim = (dataDevolucao == null) ? LocalDate.now() : dataDevolucao;

        long dias = ChronoUnit.DAYS.between(dataEmprestimo, fim);
        long atraso = dias - PRAZO_DIAS;

        if (atraso > 0) {
            multa = atraso * VALOR_DIA_ATRASO;
        } else {
            multa = 0.0;
        }
        return multa;
    }

    /*
      Converte o objeto para linha CSV.
      Formato completo com todos os campos exigidos:
     */
    @Override
    public String toString() {
        return String.format(
                "Livro: %s | Usuário: %s\n"
                + "Empréstimo: %s | Prevista: %s | Devolução: %s\n"
                + "%s",
                livro != null ? livro.getTitulo() : "N/A",
                usuario != null ? usuario.getNome() : "N/A",
                dataEmprestimoFmt(),
                dataPrevistaFmt(),
                dataDevolucaoFmt(),
                situacaoPrazo()
        );
    }

    // id;idLivro;idUsuario;codigoLivro;matriculaUsuario;dataEmprestimo;dataPrevista;dataDevolucao;multa
    public String toCSV() {
        String dev = (dataDevolucao == null ? "" : dataDevolucao.toString());

        return id + ";"
                + idLivro + ";"
                + idUsuario + ";"
                + codigoLivro + ";"
                + matriculaUsuario + ";"
                + dataEmprestimo + ";"
                + dataPrevista + ";"
                + dev + ";"
                + multa;
    }

    // Constrói um empréstimo a partir de uma linha de CSV.
    public static Emprestimo fromCSV(String linha) {

        try {
            String[] p = linha.split(";", -1);

            int id = Integer.parseInt(p[0]);
            int idLivro = Integer.parseInt(p[1]);
            int idUsuario = Integer.parseInt(p[2]);

            String codigoLivro = p[3];
            String matriculaUsuario = p[4];

            LocalDate dataEmp = LocalDate.parse(p[5]);
            LocalDate dataPrev = LocalDate.parse(p[6]);

            LocalDate dataDev = p[7].isEmpty() ? null : LocalDate.parse(p[7]);

            double multa = p[8].isEmpty() ? 0.0 : Double.parseDouble(p[8]);

            Emprestimo e = new Emprestimo(id, codigoLivro, matriculaUsuario, dataEmp, dataPrev, dataDev, multa);

            e.idLivro = idLivro;
            e.idUsuario = idUsuario;

            return e;

        } catch (Exception e) {
            return null;
        }
    }
}
