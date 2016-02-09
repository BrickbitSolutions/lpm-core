package be.brickbit.lpm.infrastructure.mapper;

public interface CommandFeeder<Source, Target> {

    void feed(Source source, Target target);
}
