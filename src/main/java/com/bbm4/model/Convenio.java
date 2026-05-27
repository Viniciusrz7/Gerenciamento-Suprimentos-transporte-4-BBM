package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity(tableName = "convenios")
public class Convenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroConvenio;
    private String participe;
    private String tipo;
    private String objeto;
    private String recurso;
    private String beneficios;
    private Double valor;
    
    // Dates
    private LocalDate inicio;
    private LocalDate termino;
    
    private String situacao;
    private String termosAditivos;
    private String numeroProcessoSei;
    
    @Column(length = 1000)
    private String informacoesAdicionais;

    public Convenio() {}

    public boolean isVencendoEmBreve() {
        if (termino == null) return false;
        long monthsBetween = ChronoUnit.MONTHS.between(LocalDate.now(), termino);
        return monthsBetween >= 0 && monthsBetween <= 4;
    }

    public boolean isVencido() {
        if (termino == null) return false;
        return termino.isBefore(LocalDate.now());
    }

    // Getters and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroConvenio() { return numeroConvenio; }
    public void setNumeroConvenio(String numeroConvenio) { this.numeroConvenio = numeroConvenio; }
    public String getParticipe() { return participe; }
    public void setParticipe(String participe) { this.participe = participe; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getObjeto() { return objeto; }
    public void setObjeto(String objeto) { this.objeto = objeto; }
    public String getRecurso() { return recurso; }
    public void setRecurso(String recurso) { this.recurso = recurso; }
    public String getBeneficios() { return beneficios; }
    public void setBeneficios(String beneficios) { this.beneficios = beneficios; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public LocalDate getTermino() { return termino; }
    public void setTermino(LocalDate termino) { this.termino = termino; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getTermosAditivos() { return termosAditivos; }
    public void setTermosAditivos(String termosAditivos) { this.termosAditivos = termosAditivos; }
    public String getNumeroProcessoSei() { return numeroProcessoSei; }
    public void setNumeroProcessoSei(String numeroProcessoSei) { this.numeroProcessoSei = numeroProcessoSei; }
    public String getInformacoesAdicionais() { return informacoesAdicionais; }
    public void setInformacoesAdicionais(String informacoesAdicionais) { this.informacoesAdicionais = informacoesAdicionais; }
}
