/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidad.Producto;
import java.util.List;
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
    
    public List<Producto> productosConStock() {
        List<Producto> productos = productoJpa.findProductoEntities();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getStock() == 0) {
                productos.remove(i);
            }
        }
        return productos;
    }
}
