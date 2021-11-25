package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("SELECT u FROM User u ORDER BY u.id" +
                "", User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByName(String name) {

        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.firstName = :name ", User.class)
                .setParameter("name", name).getSingleResult();
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public Set<Role> getRoles(Long id) {
        User user = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id ", User.class)
                .setParameter("id", id).getSingleResult();
        return user.getRoles();

    }

    @Override
    public void setRoleByName(User user, String roleName) {
        // User user1 = getUserById(user.getId());
        Set<Role> roles = getRoles(user.getId());
        List<Role> lstRole = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :roleName", Role.class).setParameter("roleName", roleName).getResultList();
        if (lstRole.size() > 0) {
            roles.add(lstRole.get(0));
        } else {
            roles.add(new Role(roleName));
        }
        user.setRoles(roles);
    }
    @Override
    public void setRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
    }

}
