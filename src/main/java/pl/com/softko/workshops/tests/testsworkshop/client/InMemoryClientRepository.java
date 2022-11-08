package pl.com.softko.workshops.tests.testsworkshop.client;

import org.springframework.stereotype.Repository;

import java.util.Map;

import static pl.com.softko.workshops.tests.testsworkshop.client.ClientType.NORMAL;
import static pl.com.softko.workshops.tests.testsworkshop.client.ClientType.REGULAR;
import static pl.com.softko.workshops.tests.testsworkshop.client.ClientType.VIP;

@Repository
class InMemoryClientRepository implements ClientRepository {
    private static Map<Long, ClientType> CLIENTS_MAP = Map.of(
            12L, NORMAL,
            20L, VIP,
            30L, REGULAR
    );

    @Override
    public ClientType getClientKindById(long id) {
        return CLIENTS_MAP.getOrDefault(id, NORMAL);
    }
}
