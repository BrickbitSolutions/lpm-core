package be.brickbit.lpm.infrastructure;

import be.brickbit.lpm.infrastructure.mapper.Extractor;
import be.brickbit.lpm.infrastructure.mapper.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class AbstractTypeService<Entity, Type extends Serializable> implements TypeService<Entity, Type> {

    protected  <S, T> List<T> map(Collection<S> source, Mapper<S, T> mapper) {
        return source
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T findAllExtracted(Extractor<Entity, T> extractor) {
        List<Entity> entities = getRepository().findAll();
        return extractor.extract(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(Mapper<Entity, T> mapper) {
        List<Entity> entities = getRepository().findAll();
        return map(entities, mapper);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(Mapper<Entity, T> mapper, Sort sort) {
        List<Entity> entities = getRepository().findAll(sort);
        return map(entities, mapper);
    }


    @Override
    @Transactional(readOnly = true)
    public <T> T findOne(Type id, Mapper<Entity, T> mapper) {
        return mapper.map(getRepository().findOne(id));
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findAll(List<Type> ids,Mapper<Entity, T> mapper) {
        List<Entity> entities = getRepository().findAll(ids);
        return map(entities, mapper);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> void feedCommand(Type id, T command, BiConsumer<? super Entity, T> commandFeeder) {
        Entity entity = getRepository().findOne(id);
        commandFeeder.accept(entity, command);

    }

    protected abstract <T extends JpaRepository<Entity, Type>> T getRepository();
}
