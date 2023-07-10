package ship.game.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateDemo {


    public static void main(String[] args) {
// TODO configurate hibernate using .xml file
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        PlayerEntity playerEntity = new PlayerEntity("RED", 1, 20);
        session.save(playerEntity);

        transaction.commit();
        PlayerEntity player2 = session.find(PlayerEntity.class, 199);
        System.out.println(player2);
        List<PlayerEntity> players = session.createQuery("Select * from players;", PlayerEntity.class).getResultList();
        System.out.println(players);
        session.close();
    }

}
