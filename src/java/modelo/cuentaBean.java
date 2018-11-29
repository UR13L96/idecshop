/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ClienteFacade;
import entidad.Cliente;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Named(value = "cuenta")
@RequestScoped
public class cuentaBean {
private String nombre;
private String apellidos;
private String correo;
private String telefono;
ClienteFacade clienteFa;
int id_cliente;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    HttpSession sesion;
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
   
    public cuentaBean() {
    }
    
    public Cliente obtenerCuenta(){
         clienteFa = new ClienteFacade();
        sesion = (HttpSession) ec.getSession(false);
         id_cliente=(int)sesion.getAttribute("cliente_id");
          Cliente cliente2 = clienteFa.buscarPorid(id_cliente);
          System.out.println("hola");
       return cliente2;
    }
    
}
