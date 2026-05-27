package com.bbm4.model;

import com.bbm4.orm.*;

@Entity
public class CartaoAbastecimento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String nomeCartaoCoringa;
    @ManyToOne private Viatura viaturaVinculada;
    private String situacao; // Vinculado, Não Vinculado

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCartaoCoringa() { return nomeCartaoCoringa; }
    public void setNomeCartaoCoringa(String nomeCartaoCoringa) { this.nomeCartaoCoringa = nomeCartaoCoringa; }
    public Viatura getViaturaVinculada() { return viaturaVinculada; }
    public void setViaturaVinculada(Viatura viaturaVinculada) { this.viaturaVinculada = viaturaVinculada; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
}
