/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


import General.Validaciones;
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
    private String contrasena2;
    private String nombre;
    private String apellidos;
    private String telefono;
    ClienteFacade clienteFa;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    HttpSession sesion;
    Validaciones validar= new Validaciones();
    int id_cliente;
    public clienteBean() {
    }

    public String getContrasena2() {
        return contrasena2;
    }

    public void setContrasena2(String contrasena2) {
        this.contrasena2 = contrasena2;
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

    public void registrar() throws Exception {
        clienteFa = new ClienteFacade();
        if (validar.valCadena(nombre) == true) {
            if (validar.valCadena(apellidos) == true) {
                if (validar.valEmail(correo) == true) {
                    if (validar.valNumEntero(telefono) == true) {
                        if (contrasena.equals(contrasena2) == true) {
                            Cliente cliente = new Cliente(nombre, apellidos, correo, telefono, contrasena);
                            clienteFa.registrar(cliente);

                            ec.redirect(ec.getRequestContextPath() + "/faces/iniciar.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "La contraseña no coincide"));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El telefono debe de ser numerico"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El correo esta mal el formato es así usuario@gmail.com"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El apellido esta mal"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "El nombres esta mal"));
        }
    }

    public void autenticar() throws IOException {
        clienteFa = new ClienteFacade();
        Cliente cliente = clienteFa.buscarPorCorreo(correo);

        if (cliente != null) {
            if (cliente.getContrasena().equals(contrasena)) {
                cambiaSession();

                sesion = (HttpSession) ec.getSession(false);
                sesion.setAttribute("cliente_id",cliente.getId());
                System.out.println(sesion.getAttribute("cliente_id"));
                id_cliente=(int)sesion.getAttribute("cliente_id");
                  
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "BIENVENIDO", "BIENVENIDO" + cliente.getNombre()));
                ec.redirect(ec.getRequestContextPath() + "/faces/cctv.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "La contraseña no coincide", "La contraseña no coincide"));

        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "El usuario no existe", "El usuario no existe"));
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

    
    public boolean clienteValidado() {
        HttpSession session = (HttpSession) ec.getSession(true);
        return session.getAttribute("cliente_id") != null;
       
    }

    public void cerrarSesion() throws IOException {
        sesion = (HttpSession) ec.getSession(false);
        sesion.invalidate();
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
    }
    
    public void catalogo() throws IOException
    {
        HttpSession session = (HttpSession) ec.getSession(false);
        if (session.getAttribute("cliente_id") != null) {
            ec.redirect(ec.getRequestContextPath() + "/faces/carrito.xhtml");
        }
        else{
            ec.redirect(ec.getRequestContextPath() + "/faces/catalogo.xhtml");
        }
    }
}
