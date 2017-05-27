package com.simplifi.it.rt.dag;

/**
 * Created by tfarkas on 5/27/17.
 */
public class Edge<T> {
  private T src;
  private T dest;

  public Edge(T src, T dest)
  {
  }

  public T getSrc()
  {
    return src;
  }

  public T getDest()
  {
    return dest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Edge<?> edge = (Edge<?>) o;

    if (!src.equals(edge.src)) return false;
    return dest.equals(edge.dest);
  }

  @Override
  public int hashCode() {
    int result = src.hashCode();
    result = 31 * result + dest.hashCode();
    return result;
  }
}
