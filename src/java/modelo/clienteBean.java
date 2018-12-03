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
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;

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
                            contrasena=(encripta(contrasena));
                            Cliente cliente = new Cliente(
                                    nombre, 
                                    apellidos, 
                                    correo, 
                                    telefono, 
                                    contrasena);
                            clienteFa.registrar(cliente);
                         
                            ec.redirect(ec.getRequestContextPath() + "/faces/iniciar.xhtml");
                        } else {
                            FacesContext.getCurrentInstance().addMessage(
                                    null, 
                                    new FacesMessage(
                                            FacesMessage.SEVERITY_FATAL, 
                                            "Error", 
                                            "La contraseña no coincide"));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(
                                null, 
                                new FacesMessage(
                                        FacesMessage.SEVERITY_FATAL, 
                                        "Error", 
                                        "El telefono debe de ser numerico"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            null, 
                            new FacesMessage(
                                    FacesMessage.SEVERITY_FATAL, 
                                    "Error", 
                                    "El correo esta mal el formato es así usuario@gmail.com"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null, 
                        new FacesMessage(
                                FacesMessage.SEVERITY_FATAL, 
                                "Error", 
                                "El apellido esta mal"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(
                            FacesMessage.SEVERITY_FATAL, 
                            "Error", 
                            "El nombres esta mal"));
        }
    }
    
    public void autenticar() throws IOException {
        clienteFa = new ClienteFacade();
        Cliente cliente = clienteFa.buscarPorCorreo(correo);
        ArrayList<Carrito> carrito = new ArrayList();
        
        if (cliente != null) {
              String contraDesen = Desencriptar(cliente.getContrasena());
            if (contraDesen.equals(contrasena)) {
                cambiaSession();

                sesion = (HttpSession) ec.getSession(false);
                sesion.setAttribute("validado", true);
                sesion.setAttribute("carrito", carrito);
                
                sesion.setAttribute("cliente_id",cliente.getId());
                System.out.println(sesion.getAttribute("cliente_id"));
                id_cliente=(int)sesion.getAttribute("cliente_id");
                  
                FacesContext.getCurrentInstance().addMessage(
                        null, new FacesMessage(
                                FacesMessage.SEVERITY_FATAL, 
                                "BIENVENIDO", 
                                "BIENVENIDO" + cliente.getNombre()));
                ec.redirect(ec.getRequestContextPath() + "/faces/cuenta.xhtml");
            }
            FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(
                        FacesMessage.SEVERITY_FATAL, 
                        "La contraseña no coincide", 
                        "La contraseña no coincide"));

        }
        FacesContext.getCurrentInstance().addMessage(
                null, 
                new FacesMessage(
                        FacesMessage.SEVERITY_FATAL, 
                        "El usuario no existe", 
                        "El usuario no existe"));
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
    
    public String encripta(String cadena) {
        String texto = cadena;
        System.out.println("Esta es sin encriptar: " + texto);
        String secretKey = "qualityinfosolutions"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        System.out.println("Esta es encriptada " + base64EncryptedString);
        return base64EncryptedString;
    }

    public String Desencriptar(String textoEncriptado) {
        String secretKey = "qualityinfosolutions"; //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

            System.out.println("Esta es desencriptada 1" + base64EncryptedString);
        } catch (Exception ex) {
            System.out.println("Esta es desencriptada 2" + base64EncryptedString);
        }
        System.out.println("Esta es desencriptada 12" + base64EncryptedString);
        return base64EncryptedString;
    }
    
    public void renderizarPagina() throws IOException {
        if (!clienteValidado()) {               
            ec.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            ec.dispatch("/error.xhtml");
            fc.responseComplete();
        }
    }
}
