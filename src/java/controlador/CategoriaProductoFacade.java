/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidad.CategoriaProducto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author uriel
 */
public class CategoriaProductoFacade {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("idecshopPU");
    private CategoriaProductoJpaController categoriaProductoJpa = new CategoriaProductoJpaController(emf);

    CategoriaProducto categoriaProducto;
    
    public CategoriaProductoFacade() {
        
    }
    
}
