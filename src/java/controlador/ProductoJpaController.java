/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.CategoriaProducto;
import entidad.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author uriel
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CategoriaProducto fkIdCategoriaProducto = producto.getFkIdCategoriaProducto();
            if (fkIdCategoriaProducto != null) {
                fkIdCategoriaProducto = em.getReference(fkIdCategoriaProducto.getClass(), fkIdCategoriaProducto.getId());
                producto.setFkIdCategoriaProducto(fkIdCategoriaProducto);
            }
            em.persist(producto);
            if (fkIdCategoriaProducto != null) {
                fkIdCategoriaProducto.getProductoCollection().add(producto);
                fkIdCategoriaProducto = em.merge(fkIdCategoriaProducto);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getId());
            CategoriaProducto fkIdCategoriaProductoOld = persistentProducto.getFkIdCategoriaProducto();
            CategoriaProducto fkIdCategoriaProductoNew = producto.getFkIdCategoriaProducto();
            if (fkIdCategoriaProductoNew != null) {
                fkIdCategoriaProductoNew = em.getReference(fkIdCategoriaProductoNew.getClass(), fkIdCategoriaProductoNew.getId());
                producto.setFkIdCategoriaProducto(fkIdCategoriaProductoNew);
            }
            producto = em.merge(producto);
            if (fkIdCategoriaProductoOld != null && !fkIdCategoriaProductoOld.equals(fkIdCategoriaProductoNew)) {
                fkIdCategoriaProductoOld.getProductoCollection().remove(producto);
                fkIdCategoriaProductoOld = em.merge(fkIdCategoriaProductoOld);
            }
            if (fkIdCategoriaProductoNew != null && !fkIdCategoriaProductoNew.equals(fkIdCategoriaProductoOld)) {
                fkIdCategoriaProductoNew.getProductoCollection().add(producto);
                fkIdCategoriaProductoNew = em.merge(fkIdCategoriaProductoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            CategoriaProducto fkIdCategoriaProducto = producto.getFkIdCategoriaProducto();
            if (fkIdCategoriaProducto != null) {
                fkIdCategoriaProducto.getProductoCollection().remove(producto);
                fkIdCategoriaProducto = em.merge(fkIdCategoriaProducto);
            }
            em.remove(producto);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
