/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author uriel
 */
@Entity
@Table(name = "producto_orden")
@NamedQueries({
    @NamedQuery(name = "ProductoOrden.findAll", query = "SELECT p FROM ProductoOrden p")
    , @NamedQuery(name = "ProductoOrden.findById", query = "SELECT p FROM ProductoOrden p WHERE p.id = :id")
    , @NamedQuery(name = "ProductoOrden.findByNombre", query = "SELECT p FROM ProductoOrden p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "ProductoOrden.findByPrecio", query = "SELECT p FROM ProductoOrden p WHERE p.precio = :precio")})
public class ProductoOrden implements Serializable {

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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private BigDecimal precio;
    @JoinColumn(name = "fk_id_producto", referencedColumnName = "id")
    @ManyToOne
    private Producto fkIdProducto;

    public ProductoOrden() {
    }

    public ProductoOrden(Integer id) {
        this.id = id;
    }

    public ProductoOrden(Integer id, String nombre, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Producto getFkIdProducto() {
        return fkIdProducto;
    }

    public void setFkIdProducto(Producto fkIdProducto) {
        this.fkIdProducto = fkIdProducto;
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
        if (!(object instanceof ProductoOrden)) {
            return false;
        }
        ProductoOrden other = (ProductoOrden) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.ProductoOrden[ id=" + id + " ]";
    }
    
}
