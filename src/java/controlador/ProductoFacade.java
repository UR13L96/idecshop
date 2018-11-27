/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidad.Producto;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author uriel
 */
public class ProductoFacade {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("idecshopPU");
    private ProductoJpaController productoJpa = new ProductoJpaController(emf);
    
    Producto producto;

    public ProductoFacade() {
        
    }
    
}
