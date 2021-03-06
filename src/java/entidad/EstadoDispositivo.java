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
@Table(name = "estado_dispositivo")
@NamedQueries({
    @NamedQuery(name = "EstadoDispositivo.findAll", query = "SELECT e FROM EstadoDispositivo e")
    , @NamedQuery(name = "EstadoDispositivo.findById", query = "SELECT e FROM EstadoDispositivo e WHERE e.id = :id")
    , @NamedQuery(name = "EstadoDispositivo.findByNombre", query = "SELECT e FROM EstadoDispositivo e WHERE e.nombre = :nombre")})
public class EstadoDispositivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "fkIdEstadoDispositivo")
    private List<HistorialDispositivo> historialDispositivoList;

    public EstadoDispositivo() {
    }

    public EstadoDispositivo(Integer id) {
        this.id = id;
    }

    public EstadoDispositivo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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
        if (!(object instanceof EstadoDispositivo)) {
            return false;
        }
        EstadoDispositivo other = (EstadoDispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.EstadoDispositivo[ id=" + id + " ]";
    }
    
}
