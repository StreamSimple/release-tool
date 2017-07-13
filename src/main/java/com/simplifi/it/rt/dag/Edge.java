package com.simplifi.it.rt.dag;

import com.google.common.base.Preconditions;

public class Edge<T> {
  private T src;
  private T dest;

  public Edge(T src, T dest)
  {
    this.src = Preconditions.checkNotNull(src);
    this.dest = Preconditions.checkNotNull(dest);
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
  public String toString()
  {
    return "Edge{" +
      "src=" + src +
      ", dest=" + dest +
      '}';
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
