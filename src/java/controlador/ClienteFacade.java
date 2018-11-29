/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import entidad.Cliente;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClienteFacade {
  
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("idecshopPU");
    private ClienteJpaController clienteJpa = new ClienteJpaController(emf);
     Cliente cliente;
   

    public ClienteFacade() {
    }
     
    public void registrar(Cliente cliente) throws Exception{
        cliente.setNombre(cliente.getNombre());
        cliente.setApellidos(cliente.getApellidos());
        cliente.setCorreo(cliente.getCorreo());
        cliente.setTelefono(cliente.getTelefono());
        cliente.setContrasena(cliente.getContrasena());
        clienteJpa.create(cliente);
    }
    
    public Cliente buscarPorCorreo(String correo) {
        cliente = clienteJpa.encontrarUsuarioxLogin(correo);
        return cliente;
    }
    
     public Cliente buscarPorid(int id) {
        cliente = clienteJpa.encontrarUsuarioxId(id);
        return cliente;
    }
    
     
}
