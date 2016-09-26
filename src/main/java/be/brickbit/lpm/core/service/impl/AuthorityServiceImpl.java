package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.service.api.authority.AuthorityDtoMapper;
import be.brickbit.lpm.core.service.api.authority.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public <T> List<T> findAll(AuthorityDtoMapper<T> dtoMapper) {
        return authorityRepository.findAll().stream().map(dtoMapper::map).collect(Collectors.toList());
    }
}
