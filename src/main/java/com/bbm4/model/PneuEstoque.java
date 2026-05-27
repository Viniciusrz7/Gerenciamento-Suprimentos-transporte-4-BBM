package com.bbm4.model;

import com.bbm4.orm.*;

@Entity
public class PneuEstoque {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String medida;
    private Integer quantidade;
    private String dotVencimento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getDotVencimento() { return dotVencimento; }
    public void setDotVencimento(String dotVencimento) { this.dotVencimento = dotVencimento; }
}
