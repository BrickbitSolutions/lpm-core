package be.brickbit.lpm.infrastructure.mapper;

@FunctionalInterface
public interface Mapper<Source, Target> {
    Target map(Source source);
}
