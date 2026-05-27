package com.bbm4.model;

import com.bbm4.orm.*;
import java.time.LocalDate;

@Entity(tableName = "servicos_feitos")
public class ServicoFeito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Viatura viatura;

    private String problemaApresentado;
    private LocalDate data;
    private Double km;
    private String localConserto;
    private String status;

    public ServicoFeito() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Viatura getViatura() { return viatura; }
    public void setViatura(Viatura viatura) { this.viatura = viatura; }

    public String getProblemaApresentado() { return problemaApresentado; }
    public void setProblemaApresentado(String problemaApresentado) { this.problemaApresentado = problemaApresentado; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Double getKm() { return km; }
    public void setKm(Double km) { this.km = km; }

    public String getLocalConserto() { return localConserto; }
    public void setLocalConserto(String localConserto) { this.localConserto = localConserto; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
