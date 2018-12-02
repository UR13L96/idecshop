/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ClienteFacade;
import controlador.ContactoJpaController;
import java.io.Serializable;
import javax.inject.Named;
import entidad.Contacto;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author leonardo
 */
@Named(value = "contactoBean")
@RequestScoped
public class contactoBean implements Serializable{

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
private String correo;
private String mensaje;
    

private FacesContext fc = FacesContext.getCurrentInstance();
    private ExternalContext ec = fc.getExternalContext();
    ClienteFacade fa = new ClienteFacade();
    Contacto con;
    
public void enviar() throws Exception{
        con=new Contacto();
        con.setNombre(correo);
        con.setMensaje(mensaje);
        fa.insertaJPA(con);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Mensaje", "Mensaje enviado"));
        ec.redirect(ec.getRequestContextPath()+"/faces/index.xhtml");
    }
public void enviarpago() throws Exception{
        ec.redirect(ec.getRequestContextPath()+"/faces/pagodiagnostico.xhtml");
    }

/**
     * Creates a new instance of contactoBean
     */
    public contactoBean() {
    }
    
}
