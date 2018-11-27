/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author uriel
 */
@Named(value = "product")
@RequestScoped
public class productBean {
    
    
    /**
     * Creates a new instance of productBean
     */
    public productBean() { 
    }
}