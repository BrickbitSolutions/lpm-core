package be.brickbit.lpm.infrastructure.mapper;

public interface Mapper<Source, Target> {
    public Target map(Source source);
}
