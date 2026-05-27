package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;

@Entity(tableName = "revisao")
public class Revisao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private Viatura viatura;
    
    private String proximaRevisao;
    private Double kmRevisao;
    private String situacaoRevisao;
    private String localUltimaRevisao;
    private String oQueFoiFeito;
    private String tempoGarantia;
    private LocalDate dataRevisao;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Viatura getViatura() { return viatura; }
    public void setViatura(Viatura viatura) { this.viatura = viatura; }
    public String getProximaRevisao() { return proximaRevisao; }
    public void setProximaRevisao(String proximaRevisao) { this.proximaRevisao = proximaRevisao; }
    public Double getKmRevisao() { return kmRevisao; }
    public void setKmRevisao(Double kmRevisao) { this.kmRevisao = kmRevisao; }
    public String getSituacaoRevisao() { return situacaoRevisao; }
    public void setSituacaoRevisao(String situacaoRevisao) { this.situacaoRevisao = situacaoRevisao; }
    public String getLocalUltimaRevisao() { return localUltimaRevisao; }
    public void setLocalUltimaRevisao(String localUltimaRevisao) { this.localUltimaRevisao = localUltimaRevisao; }
    public String getOQueFoiFeito() { return oQueFoiFeito; }
    public void setOQueFoiFeito(String oQueFoiFeito) { this.oQueFoiFeito = oQueFoiFeito; }
    public String getTempoGarantia() { return tempoGarantia; }
    public void setTempoGarantia(String tempoGarantia) { this.tempoGarantia = tempoGarantia; }
    public LocalDate getDataRevisao() { return dataRevisao; }
    public void setDataRevisao(LocalDate dataRevisao) { this.dataRevisao = dataRevisao; }
    // Compatibilidade com código antigo
    public Integer getTempoGarantiaMeses() { try{return tempoGarantia!=null?Integer.parseInt(tempoGarantia.replaceAll("[^0-9]","").trim()):null;}catch(Exception e){return null;} }
    public void setTempoGarantiaMeses(Integer v) { this.tempoGarantia = v!=null?String.valueOf(v):null; }
}
