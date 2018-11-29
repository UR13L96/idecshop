/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Arisbeth
 */
@Entity
@Table(name = "dispositivo")
@NamedQueries({
    @NamedQuery(name = "Dispositivo.findAll", query = "SELECT d FROM Dispositivo d")
    , @NamedQuery(name = "Dispositivo.findById", query = "SELECT d FROM Dispositivo d WHERE d.id = :id")
    , @NamedQuery(name = "Dispositivo.findByNombre", query = "SELECT d FROM Dispositivo d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Dispositivo.findByNumSerie", query = "SELECT d FROM Dispositivo d WHERE d.numSerie = :numSerie")
    , @NamedQuery(name = "Dispositivo.findByMarca", query = "SELECT d FROM Dispositivo d WHERE d.marca = :marca")
    , @NamedQuery(name = "Dispositivo.findByModelo", query = "SELECT d FROM Dispositivo d WHERE d.modelo = :modelo")})
public class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "num_serie")
    private String numSerie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "modelo")
    private String modelo;
    @JoinColumn(name = "fk_id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente fkIdCliente;
    @OneToMany(mappedBy = "fkIdDispositivo")
    private List<HistorialDispositivo> historialDispositivoList;

    public Dispositivo() {
    }

    public Dispositivo(Integer id) {
        this.id = id;
    }

    public Dispositivo(Integer id, String nombre, String numSerie, String marca, String modelo) {
        this.id = id;
        this.nombre = nombre;
        this.numSerie = numSerie;
        this.marca = marca;
        this.modelo = modelo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Cliente getFkIdCliente() {
        return fkIdCliente;
    }

    public void setFkIdCliente(Cliente fkIdCliente) {
        this.fkIdCliente = fkIdCliente;
    }

    public List<HistorialDispositivo> getHistorialDispositivoList() {
        return historialDispositivoList;
    }

    public void setHistorialDispositivoList(List<HistorialDispositivo> historialDispositivoList) {
        this.historialDispositivoList = historialDispositivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dispositivo)) {
            return false;
        }
        Dispositivo other = (Dispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Dispositivo[ id=" + id + " ]";
    }
    
}
