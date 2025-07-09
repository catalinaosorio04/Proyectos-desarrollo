package com.desarrolloweb.comercial.model.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desarrolloweb.comercial.model.dao.UsuarioDAOIface;
import com.desarrolloweb.comercial.model.entity.Rol;
import com.desarrolloweb.comercial.model.entity.Usuario;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetailService implements UserDetailsService{

    private final Logger logger = LoggerFactory.getLogger(getClass()); // mensajes por consola
    private final UsuarioDAOIface usuarioDAO;

    public UsuarioDetailService(UsuarioDAOIface usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // consultar el usuario que se autentica
		Usuario usuario = usuarioDAO.findByNombre(username);
		
        // validar que usuario esté en la base de datos
		if (usuario == null) {
            logger.error("*** Error de autenticación, el usuario '" + username + "' no existe");
			throw new UsernameNotFoundException("*** Error de autenticación, el usuario con el nombre " + username + " no existe !!");
		}
		
        // si el usuario está en la base de datos obtener sus roles
		List<GrantedAuthority> roles = new ArrayList<>();
		for(Rol rol : usuario.getRoles()) {
			logger.info("**** Rol: " + rol.getNombre());
			roles.add(new SimpleGrantedAuthority(rol.getNombre()));
		}
		
        // si la lista de roles está vacía...
		if (roles.isEmpty()) {
			logger.warn("El usuario " + usuario.getNombre() + " no tiene roles asignados");
			throw new UsernameNotFoundException("El usuario " + usuario.getNombre() + " no tiene roles asignados !!");
		}
		
        // retornar un usuario de tipo User de Security
		return new User(usuario.getNombre(), usuario.getClave(), usuario.isActivo(), true, true, true, roles);
	}
}