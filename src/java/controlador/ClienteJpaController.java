/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Contacto;
import java.util.ArrayList;
import java.util.Collection;
import entidad.Orden;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author uriel
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(Cliente cliente) throws Exception {
        EntityManager em = null;
        try {
           
        em = getEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("creates: " + ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Cliente encontrarUsuarioxLogin(String correo){
        EntityManager em = null;
        em = getEntityManager();
        Query q = em.createNamedQuery("Cliente.findByCorreo", Cliente.class).setParameter("correo", correo);
        List<Cliente> listado = q.getResultList();
        if(!listado.isEmpty()){
            return listado.get(0);
        }
        return null;
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            Collection<Contacto> contactoCollectionOld = persistentCliente.getContactoCollection();
            Collection<Contacto> contactoCollectionNew = cliente.getContactoCollection();
            Collection<Orden> ordenCollectionOld = persistentCliente.getOrdenCollection();
            Collection<Orden> ordenCollectionNew = cliente.getOrdenCollection();
            Collection<Contacto> attachedContactoCollectionNew = new ArrayList<Contacto>();
            for (Contacto contactoCollectionNewContactoToAttach : contactoCollectionNew) {
                contactoCollectionNewContactoToAttach = em.getReference(contactoCollectionNewContactoToAttach.getClass(), contactoCollectionNewContactoToAttach.getId());
                attachedContactoCollectionNew.add(contactoCollectionNewContactoToAttach);
            }
            contactoCollectionNew = attachedContactoCollectionNew;
            cliente.setContactoCollection(contactoCollectionNew);
            Collection<Orden> attachedOrdenCollectionNew = new ArrayList<Orden>();
            for (Orden ordenCollectionNewOrdenToAttach : ordenCollectionNew) {
                ordenCollectionNewOrdenToAttach = em.getReference(ordenCollectionNewOrdenToAttach.getClass(), ordenCollectionNewOrdenToAttach.getId());
                attachedOrdenCollectionNew.add(ordenCollectionNewOrdenToAttach);
            }
            ordenCollectionNew = attachedOrdenCollectionNew;
            cliente.setOrdenCollection(ordenCollectionNew);
            cliente = em.merge(cliente);
            for (Contacto contactoCollectionOldContacto : contactoCollectionOld) {
                if (!contactoCollectionNew.contains(contactoCollectionOldContacto)) {
                    contactoCollectionOldContacto.setFkIdCliente(null);
                    contactoCollectionOldContacto = em.merge(contactoCollectionOldContacto);
                }
            }
            for (Contacto contactoCollectionNewContacto : contactoCollectionNew) {
                if (!contactoCollectionOld.contains(contactoCollectionNewContacto)) {
                    Cliente oldFkIdClienteOfContactoCollectionNewContacto = contactoCollectionNewContacto.getFkIdCliente();
                    contactoCollectionNewContacto.setFkIdCliente(cliente);
                    contactoCollectionNewContacto = em.merge(contactoCollectionNewContacto);
                    if (oldFkIdClienteOfContactoCollectionNewContacto != null && !oldFkIdClienteOfContactoCollectionNewContacto.equals(cliente)) {
                        oldFkIdClienteOfContactoCollectionNewContacto.getContactoCollection().remove(contactoCollectionNewContacto);
                        oldFkIdClienteOfContactoCollectionNewContacto = em.merge(oldFkIdClienteOfContactoCollectionNewContacto);
                    }
                }
            }
            for (Orden ordenCollectionOldOrden : ordenCollectionOld) {
                if (!ordenCollectionNew.contains(ordenCollectionOldOrden)) {
                    ordenCollectionOldOrden.setFkIdCliente(null);
                    ordenCollectionOldOrden = em.merge(ordenCollectionOldOrden);
                }
            }
            for (Orden ordenCollectionNewOrden : ordenCollectionNew) {
                if (!ordenCollectionOld.contains(ordenCollectionNewOrden)) {
                    Cliente oldFkIdClienteOfOrdenCollectionNewOrden = ordenCollectionNewOrden.getFkIdCliente();
                    ordenCollectionNewOrden.setFkIdCliente(cliente);
                    ordenCollectionNewOrden = em.merge(ordenCollectionNewOrden);
                    if (oldFkIdClienteOfOrdenCollectionNewOrden != null && !oldFkIdClienteOfOrdenCollectionNewOrden.equals(cliente)) {
                        oldFkIdClienteOfOrdenCollectionNewOrden.getOrdenCollection().remove(ordenCollectionNewOrden);
                        oldFkIdClienteOfOrdenCollectionNewOrden = em.merge(oldFkIdClienteOfOrdenCollectionNewOrden);
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
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            Collection<Contacto> contactoCollection = cliente.getContactoCollection();
            for (Contacto contactoCollectionContacto : contactoCollection) {
                contactoCollectionContacto.setFkIdCliente(null);
                contactoCollectionContacto = em.merge(contactoCollectionContacto);
            }
            Collection<Orden> ordenCollection = cliente.getOrdenCollection();
            for (Orden ordenCollectionOrden : ordenCollection) {
                ordenCollectionOrden.setFkIdCliente(null);
                ordenCollectionOrden = em.merge(ordenCollectionOrden);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
