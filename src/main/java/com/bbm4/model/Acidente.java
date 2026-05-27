package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;

@Entity
public class Acidente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private LocalDate dataAcidente;
    @ManyToOne private Viatura viatura;
    private String partePrejudicada;
    private String local;
    private String reds;
    private String sei;
    private String motorista;
    private String statusAndamento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDataAcidente() { return dataAcidente; }
    public void setDataAcidente(LocalDate dataAcidente) { this.dataAcidente = dataAcidente; }
    public Viatura getViatura() { return viatura; }
    public void setViatura(Viatura viatura) { this.viatura = viatura; }
    public String getPartePrejudicada() { return partePrejudicada; }
    public void setPartePrejudicada(String partePrejudicada) { this.partePrejudicada = partePrejudicada; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getReds() { return reds; }
    public void setReds(String reds) { this.reds = reds; }
    public String getSei() { return sei; }
    public void setSei(String sei) { this.sei = sei; }
    public String getMotorista() { return motorista; }
    public void setMotorista(String motorista) { this.motorista = motorista; }
    public String getStatusAndamento() { return statusAndamento; }
    public void setStatusAndamento(String statusAndamento) { this.statusAndamento = statusAndamento; }
}
