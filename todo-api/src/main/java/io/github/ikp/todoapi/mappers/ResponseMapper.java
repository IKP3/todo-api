package io.github.ikp.todoapi.mappers;

public interface ResponseMapper<A,B> {
  B mapTo(A a);

}
