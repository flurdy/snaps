package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.SecurityAuthority;
import com.flurdy.grid.snaps.domain.SecurityAuthority.AuthorityRole;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SecurityRepository extends AbstractRepository implements ISecurityRepository {

	@Override
	public SecurityDetail findSecurityDetail(String username) {

        assert username != null;
        assert username.length()>0;

		Query query = entityManager.createNamedQuery("securitydetail.findSecurityDetailByUsername");
		query.setParameter("username", username);
		try{
			return (SecurityDetail) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}	
	}

	@Override
	public SecurityAuthority findAuthority(AuthorityRole role) {
        assert role != null;
		Query query = entityManager.createNamedQuery("authority.findAuthorityByRole");
		query.setParameter("authorityRole", role.toString());
		try{
			return (SecurityAuthority) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			log.error("Too many authorities: "+role);
//			new ex;
		}
	}

	@Override
	public void addAuthority(SecurityAuthority authority) {
		assert authority != null;
		entityManager.persist(authority);
        // entityManager.flush();
	}

	@Override
	public void addSecurityDetail(SecurityDetail securityDetail) {
		assert securityDetail != null;
		entityManager.persist(securityDetail);
	}

}
