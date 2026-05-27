package com.bbm4.model;

import com.bbm4.orm.*;

@Entity(tableName = "modelo_sei")
public class ModeloSei {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroSei;
    private String modelo;

    public ModeloSei() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroSei() { return numeroSei; }
    public void setNumeroSei(String numeroSei) { this.numeroSei = numeroSei; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
}
