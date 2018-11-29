/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author uriel
 */
@Named(value = "carrito")
@RequestScoped
public class carritoBean {
    private ArrayList<Carrito> carrito = null;
    private double total;
    
    HttpSession sesion;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    
    
    /**
     * Creates a new instance of carritoBean
     */
    public carritoBean() {
    }
    
    public void agregarAlCarrito(int id, int cantidad, double precio, String nombre)
    {
        sesion = (HttpSession) ec.getSession(false);
        carrito = (ArrayList<Carrito>)sesion.getAttribute("carrito");
        
        Carrito car = new Carrito(id, cantidad, precio, nombre);
        carrito.add(car);
        sesion.setAttribute("carrito", carrito);
    }
    
    public ArrayList<Carrito> obtenerCarrito()
    {
        sesion = (HttpSession) ec.getSession(false);
        carrito = (ArrayList<Carrito>)sesion.getAttribute("carrito");
        
        return carrito;
    }
    
    
    public double calculaTotal()
    {
        sesion = (HttpSession) ec.getSession(false);
        carrito = (ArrayList<Carrito>)sesion.getAttribute("carrito");
        for (int i = 0; i < carrito.size(); i++) {
            System.out.println(carrito.get(i).getId() + ", " + carrito.get(i).getCantidad());
            total += carrito.get(i).getCantidad() * carrito.get(i).getPrecio();
        }
        return total;
    }
}
