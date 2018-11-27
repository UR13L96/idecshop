/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.CategoriaProducto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Producto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author uriel
 */
public class CategoriaProductoJpaController implements Serializable {

    public CategoriaProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoriaProducto categoriaProducto) throws RollbackFailureException, Exception {
        if (categoriaProducto.getProductoCollection() == null) {
            categoriaProducto.setProductoCollection(new ArrayList<Producto>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Producto> attachedProductoCollection = new ArrayList<Producto>();
            for (Producto productoCollectionProductoToAttach : categoriaProducto.getProductoCollection()) {
                productoCollectionProductoToAttach = em.getReference(productoCollectionProductoToAttach.getClass(), productoCollectionProductoToAttach.getId());
                attachedProductoCollection.add(productoCollectionProductoToAttach);
            }
            categoriaProducto.setProductoCollection(attachedProductoCollection);
            em.persist(categoriaProducto);
            for (Producto productoCollectionProducto : categoriaProducto.getProductoCollection()) {
                CategoriaProducto oldFkIdCategoriaProductoOfProductoCollectionProducto = productoCollectionProducto.getFkIdCategoriaProducto();
                productoCollectionProducto.setFkIdCategoriaProducto(categoriaProducto);
                productoCollectionProducto = em.merge(productoCollectionProducto);
                if (oldFkIdCategoriaProductoOfProductoCollectionProducto != null) {
                    oldFkIdCategoriaProductoOfProductoCollectionProducto.getProductoCollection().remove(productoCollectionProducto);
                    oldFkIdCategoriaProductoOfProductoCollectionProducto = em.merge(oldFkIdCategoriaProductoOfProductoCollectionProducto);
                }
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

    public void edit(CategoriaProducto categoriaProducto) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CategoriaProducto persistentCategoriaProducto = em.find(CategoriaProducto.class, categoriaProducto.getId());
            Collection<Producto> productoCollectionOld = persistentCategoriaProducto.getProductoCollection();
            Collection<Producto> productoCollectionNew = categoriaProducto.getProductoCollection();
            Collection<Producto> attachedProductoCollectionNew = new ArrayList<Producto>();
            for (Producto productoCollectionNewProductoToAttach : productoCollectionNew) {
                productoCollectionNewProductoToAttach = em.getReference(productoCollectionNewProductoToAttach.getClass(), productoCollectionNewProductoToAttach.getId());
                attachedProductoCollectionNew.add(productoCollectionNewProductoToAttach);
            }
            productoCollectionNew = attachedProductoCollectionNew;
            categoriaProducto.setProductoCollection(productoCollectionNew);
            categoriaProducto = em.merge(categoriaProducto);
            for (Producto productoCollectionOldProducto : productoCollectionOld) {
                if (!productoCollectionNew.contains(productoCollectionOldProducto)) {
                    productoCollectionOldProducto.setFkIdCategoriaProducto(null);
                    productoCollectionOldProducto = em.merge(productoCollectionOldProducto);
                }
            }
            for (Producto productoCollectionNewProducto : productoCollectionNew) {
                if (!productoCollectionOld.contains(productoCollectionNewProducto)) {
                    CategoriaProducto oldFkIdCategoriaProductoOfProductoCollectionNewProducto = productoCollectionNewProducto.getFkIdCategoriaProducto();
                    productoCollectionNewProducto.setFkIdCategoriaProducto(categoriaProducto);
                    productoCollectionNewProducto = em.merge(productoCollectionNewProducto);
                    if (oldFkIdCategoriaProductoOfProductoCollectionNewProducto != null && !oldFkIdCategoriaProductoOfProductoCollectionNewProducto.equals(categoriaProducto)) {
                        oldFkIdCategoriaProductoOfProductoCollectionNewProducto.getProductoCollection().remove(productoCollectionNewProducto);
                        oldFkIdCategoriaProductoOfProductoCollectionNewProducto = em.merge(oldFkIdCategoriaProductoOfProductoCollectionNewProducto);
                    }
                }
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
                Integer id = categoriaProducto.getId();
                if (findCategoriaProducto(id) == null) {
                    throw new NonexistentEntityException("The categoriaProducto with id " + id + " no longer exists.");
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
            CategoriaProducto categoriaProducto;
            try {
                categoriaProducto = em.getReference(CategoriaProducto.class, id);
                categoriaProducto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoriaProducto with id " + id + " no longer exists.", enfe);
            }
            Collection<Producto> productoCollection = categoriaProducto.getProductoCollection();
            for (Producto productoCollectionProducto : productoCollection) {
                productoCollectionProducto.setFkIdCategoriaProducto(null);
                productoCollectionProducto = em.merge(productoCollectionProducto);
            }
            em.remove(categoriaProducto);
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

    public List<CategoriaProducto> findCategoriaProductoEntities() {
        return findCategoriaProductoEntities(true, -1, -1);
    }

    public List<CategoriaProducto> findCategoriaProductoEntities(int maxResults, int firstResult) {
        return findCategoriaProductoEntities(false, maxResults, firstResult);
    }

    private List<CategoriaProducto> findCategoriaProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoriaProducto.class));
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

    public CategoriaProducto findCategoriaProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriaProducto.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoriaProducto> rt = cq.from(CategoriaProducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
