package org.glsid.security.repository;

import org.glsid.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppUserRepository extends JpaRepository<AppUser, String>
{

	AppUser findByUserName(String userName);
}
