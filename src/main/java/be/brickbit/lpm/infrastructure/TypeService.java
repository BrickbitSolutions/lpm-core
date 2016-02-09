package be.brickbit.lpm.infrastructure;

import be.brickbit.lpm.infrastructure.mapper.Extractor;
import be.brickbit.lpm.infrastructure.mapper.Mapper;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;

public interface TypeService<Entity, Type extends Serializable> {

    <T> T findAllExtracted(Extractor<Entity, T> extractor);

    <T> List<T> findAll(Mapper<Entity, T> mapper);

    <T> List<T> findAll(Mapper<Entity, T> dtoMapper, Sort sort);

    <T> T findOne(Type id, Mapper<Entity, T> dtoMapper);

    <T> List<T> findAll(List<Type> ids, Mapper<Entity, T> dtoMapper);

    <T> void feedCommand(Type id, T command, BiConsumer<? super Entity, T> commandFeeder);
}
