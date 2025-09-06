package io.github.ikp.todoapi.mappers;

public interface RequestMapper<A,B> {
  A mapFrom(B b);
}
