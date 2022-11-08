package pl.com.softko.workshops.tests.testsworkshop.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private ClientRepository repository;

    public ClientType getClientType(long id) {
        return repository.getClientKindById(id);
    }
}
