package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void update(Role role) {
        entityManager.merge(role);
    }

    @Override
    @SuppressWarnings("Unchecked")
    public List<Role> getRoles() {
        return entityManager.createQuery("SELECT r FROM Role r ORDER BY r.id").getResultList();
    }

    @Override
    public List<Role> getRolesWithCheck(User user) {
        List lstResult =
                entityManager.createNativeQuery("""
            SELECT
                 CASE WHEN ar.id = ur.Role_id THEN TRUE ELSE FALSE END as checked,
                 ar.id AS ID,
                 ar.name as name
            FROM roles AS ar
            LEFT JOIN user_role AS ur
                ON ur.Role_id = ar.id and ur.User_id = :User_id
            """).setParameter("User_id", user.getId()).getResultList();
        List<Role> lstCheck = new ArrayList<>();
        for (Object elem : lstResult) {
            lstCheck.add(new Role((Boolean) (((BigInteger) ((Object[])elem)[0]).intValue()>0)?true:false, (Long) ((BigInteger) ((Object[])elem)[1]).longValue(), (String) ((Object[])elem)[2]));
        }
        return  lstCheck;
    }

    @Override
    public void deleteRole(Long id) {
        entityManager.remove(entityManager.find(Role.class, id));
        Query qry = entityManager.createNativeQuery("DELETE FROM user_role WHERE Role_id = :id");
        qry.setParameter("id", id);
        qry.executeUpdate();
    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Set<Role> getRolesByIdList(List<Long> ids) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.id in (:roleIds)", Role.class)
                .setParameter("roleIds", ids).getResultList()
                .stream().collect(Collectors.toSet());
    }
}
