package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;

@Entity(tableName = "viaturas")
public class Viatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String prefixo; // e.g. ABT-1234

    private String placa;
    
    private String modelo;
    
    private String sedeFracao; // juiz de fora, pemad, 3cia pv, 6 pelsul, etc
    
    private Double km;
    
    private String situacao; // Disponivel, Baixada, Descarga

    private String motivoBaixaDescarga;

    public Viatura() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrefixo() { return prefixo; }
    public void setPrefixo(String prefixo) { this.prefixo = prefixo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getSedeFracao() { return sedeFracao; }
    public void setSedeFracao(String sedeFracao) { this.sedeFracao = sedeFracao; }

    public Double getKm() { return km; }
    public void setKm(Double km) { this.km = km; }

    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }

    public String getMotivoBaixaDescarga() { return motivoBaixaDescarga; }
    public void setMotivoBaixaDescarga(String motivoBaixaDescarga) { this.motivoBaixaDescarga = motivoBaixaDescarga; }

    @Override public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof Viatura)) return false;
        Viatura v=(Viatura)o;
        return id!=null && id.equals(v.id);
    }
    @Override public int hashCode() { return id!=null?id.hashCode():0; }
}
