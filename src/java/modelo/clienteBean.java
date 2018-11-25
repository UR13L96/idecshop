/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador.ClienteFacade;
import controlador.ClienteJpaController;
import entidad.Cliente;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author uriel
 */
@Named(value = "cliente")
@RequestScoped
public class clienteBean implements Serializable {
    private Integer id;
    private String correo;
    private String contrasena;
    private String nombre;
    private String apellidos;
    private String telefono;
    
    ClienteFacade clienteFa;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    HttpSession sesion;
    
    public clienteBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void registrar() throws Exception
    {
        clienteFa = new ClienteFacade();
        
        Cliente cliente = new Cliente(nombre, apellidos, correo, telefono, contrasena);
        clienteFa.registrar(cliente);

        ec.redirect(ec.getRequestContextPath() + "/faces/iniciar.xhtml");
    }
    
     public void autenticar() throws IOException{
        clienteFa = new ClienteFacade();
        Cliente cliente = clienteFa.buscarPorCorreo(correo);
        
        if(cliente!=null){
            if(cliente.getContrasena().equals(contrasena)){
                cambiaSession();
                
                sesion = (HttpSession) ec.getSession(false);
                sesion.setAttribute("validado", true);
                
                ec.redirect(ec.getRequestContextPath() + "/faces/cctv.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"La contraseña no coincide","La contraseña no coincide"));
            
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"El usuario no existe","El usuario no existe"));                
    }
     
    private void cambiaSession() {
       sesion = (HttpSession) ec.getSession(false);
       System.out.println("Sesión nueva: " + sesion.isNew());
       System.out.println("Id sesión: " + sesion.getId());

       sesion.invalidate();

       sesion = (HttpSession) ec.getSession(true);
       System.out.println("Sesión nueva: " + sesion.isNew());
       System.out.println("Id sesión: " + sesion.getId());
   }
    
    public void validaPagina() throws IOException {
        HttpSession session = (HttpSession) ec.getSession(false);
        if (!(boolean) session.getAttribute("validado")) {
            ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
        }
        else{
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", "Información"));
        }
    }
    
    public boolean clienteValidado()
    {
        HttpSession session = (HttpSession) ec.getSession(true);
        return session.getAttribute("validado") != null;
    }
    
    public void cerrarSesion() throws IOException {
        sesion = (HttpSession) ec.getSession(false);
        sesion.invalidate();
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
    }
}
