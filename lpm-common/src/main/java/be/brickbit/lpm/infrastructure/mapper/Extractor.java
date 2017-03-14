package be.brickbit.lpm.infrastructure.mapper;


import java.util.List;

@FunctionalInterface
public interface Extractor<Source, Target> {

    Target extract(List<Source> sources);

}
