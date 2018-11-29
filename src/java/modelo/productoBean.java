/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ProductoFacade;
import entidad.Producto;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author uriel
 */
@Named(value = "producto")
@RequestScoped
public class productoBean {
    private List<Producto> productos; 
    
    ProductoFacade productoFa;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    HttpSession sesion;
    /**
     * Creates a new instance of productBean
     */
    public productoBean() { 
        productoFa = new ProductoFacade();
        productos = productoFa.productosConStock();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}