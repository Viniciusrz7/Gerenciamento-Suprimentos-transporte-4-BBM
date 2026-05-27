package com.bbm4.model;

import com.bbm4.orm.*;

@Entity(tableName = "situacao_processo_sei")
public class SituacaoProcessoSei {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assunto;
    private String numeroSei;
    private String situacaoProcesso;

    public SituacaoProcessoSei() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }

    public String getNumeroSei() { return numeroSei; }
    public void setNumeroSei(String numeroSei) { this.numeroSei = numeroSei; }

    public String getSituacaoProcesso() { return situacaoProcesso; }
    public void setSituacaoProcesso(String situacaoProcesso) { this.situacaoProcesso = situacaoProcesso; }
}