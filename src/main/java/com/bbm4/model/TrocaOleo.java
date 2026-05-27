package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;

@Entity
public class TrocaOleo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne private Viatura viatura;
    private LocalDate dataTroca;
    private Double km;
    private String filtros;
    private String observacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Viatura getViatura() { return viatura; }
    public void setViatura(Viatura viatura) { this.viatura = viatura; }
    public LocalDate getDataTroca() { return dataTroca; }
    public void setDataTroca(LocalDate dataTroca) { this.dataTroca = dataTroca; }
    public Double getKm() { return km; }
    public void setKm(Double km) { this.km = km; }
    public String getFiltros() { return filtros; }
    public void setFiltros(String filtros) { this.filtros = filtros; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
