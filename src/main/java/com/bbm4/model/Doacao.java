package com.bbm4.model;

import com.bbm4.orm.*;

@Entity
public class Doacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String itemRecebido;
    private Integer quantidade;
    private String deQuemRecebeu;
    private String numeroSei;
    private String situacaoProcesso;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemRecebido() { return itemRecebido; }
    public void setItemRecebido(String itemRecebido) { this.itemRecebido = itemRecebido; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getDeQuemRecebeu() { return deQuemRecebeu; }
    public void setDeQuemRecebeu(String deQuemRecebeu) { this.deQuemRecebeu = deQuemRecebeu; }
    public String getNumeroSei() { return numeroSei; }
    public void setNumeroSei(String numeroSei) { this.numeroSei = numeroSei; }
    public String getSituacaoProcesso() { return situacaoProcesso; }
    public void setSituacaoProcesso(String situacaoProcesso) { this.situacaoProcesso = situacaoProcesso; }
}
