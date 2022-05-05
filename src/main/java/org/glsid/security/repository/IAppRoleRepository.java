package org.glsid.security.repository;

import org.glsid.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppRoleRepository extends JpaRepository<AppRole, Long> 
{
	AppRole findByRoleName(String userRole);


}
