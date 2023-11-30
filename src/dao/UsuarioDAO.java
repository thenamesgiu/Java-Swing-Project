/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Usuario;

/**
 *
 * @author anagi
 */
public class UsuarioDAO extends GenericDAO<Usuario>{
    public boolean autenticar(String usuario, String senha){
        try{
            return getEntityManager().createNamedQuery("Usuario.autenticacao").setParameter("usuario", usuario).setParameter("senha", senha).getSingleResult() != null?true:false;
        }catch(Exception e){
            return false;
        }
        
    }
}
