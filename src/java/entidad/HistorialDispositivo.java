/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Arisbeth
 */
@Entity
@Table(name = "historial_dispositivo")
@NamedQueries({
    @NamedQuery(name = "HistorialDispositivo.findAll", query = "SELECT h FROM HistorialDispositivo h")
    , @NamedQuery(name = "HistorialDispositivo.findById", query = "SELECT h FROM HistorialDispositivo h WHERE h.id = :id")
    , @NamedQuery(name = "HistorialDispositivo.findByObservaciones", query = "SELECT h FROM HistorialDispositivo h WHERE h.observaciones = :observaciones")})
public class HistorialDispositivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "fk_id_dispositivo", referencedColumnName = "id")
    @ManyToOne
    private Dispositivo fkIdDispositivo;
    @JoinColumn(name = "fk_id_estado_dispositivo", referencedColumnName = "id")
    @ManyToOne
    private EstadoDispositivo fkIdEstadoDispositivo;

    public HistorialDispositivo() {
    }

    public HistorialDispositivo(Integer id) {
        this.id = id;
    }

    public HistorialDispositivo(Integer id, String observaciones) {
        this.id = id;
        this.observaciones = observaciones;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Dispositivo getFkIdDispositivo() {
        return fkIdDispositivo;
    }

    public void setFkIdDispositivo(Dispositivo fkIdDispositivo) {
        this.fkIdDispositivo = fkIdDispositivo;
    }

    public EstadoDispositivo getFkIdEstadoDispositivo() {
        return fkIdEstadoDispositivo;
    }

    public void setFkIdEstadoDispositivo(EstadoDispositivo fkIdEstadoDispositivo) {
        this.fkIdEstadoDispositivo = fkIdEstadoDispositivo;
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
        if (!(object instanceof HistorialDispositivo)) {
            return false;
        }
        HistorialDispositivo other = (HistorialDispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.HistorialDispositivo[ id=" + id + " ]";
    }
    
}
