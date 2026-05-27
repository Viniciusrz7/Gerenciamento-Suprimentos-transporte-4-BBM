package com.bbm4.model;

import com.bbm4.orm.*;

@Entity
public class Bateria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne private Viatura viatura;
    private String especificacaoBateria;
    private Integer tempoGarantiaMeses;
    private String observacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Viatura getViatura() { return viatura; }
    public void setViatura(Viatura viatura) { this.viatura = viatura; }
    public String getEspecificacaoBateria() { return especificacaoBateria; }
    public void setEspecificacaoBateria(String especificacaoBateria) { this.especificacaoBateria = especificacaoBateria; }
    public Integer getTempoGarantiaMeses() { return tempoGarantiaMeses; }
    public void setTempoGarantiaMeses(Integer tempoGarantiaMeses) { this.tempoGarantiaMeses = tempoGarantiaMeses; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
