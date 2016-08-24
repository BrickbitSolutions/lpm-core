package be.brickbit.lpm.infrastructure.mapper;

public interface Mapper<Source, Target> {
    Target map(Source source);
}
