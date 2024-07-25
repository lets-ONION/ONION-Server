package site.lets_onion.lets_onionApp.repository.deviceToken;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseDeviceTokenRepositoryImpl implements BaseDeviceTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> findAllDeviceTokensByMemberId(Long memberId) {
        return em.createQuery("select dt.deviceToken from DeviceToken dt" +
                " where dt.member.id =:memberId", String.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<String> findAllDeviceTokens() {
        return em.createQuery("select dt.deviceToken from DeviceToken dt",
                        String.class).getResultList();
    }

    @Modifying
    @Override
    public int deleteDeviceToken(Long memberId, String deviceToken) {
        return em.createQuery("delete from DeviceToken dt" +
                " where dt.member.id =:memberId" +
                " and dt.deviceToken =:deviceToken")
                .setParameter("memberId", memberId)
                .setParameter("deviceToken", deviceToken)
                .executeUpdate();
    }
}
