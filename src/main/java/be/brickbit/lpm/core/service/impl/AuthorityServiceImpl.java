package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import be.brickbit.lpm.infrastructure.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findByAuthority(String name) {
        return authorityRepository.findByAuthority(name).orElseThrow(
                () -> new EntityNotFoundException(String.format("Authority %s does not exist", name))
        );
    }

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }
}
