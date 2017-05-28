package com.simplifi.it.rt.dag;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Created by tfarkas on 5/27/17.
 */
public class DAG<T> implements Cloneable
{
  private SetMultimap<T, T> srcToDests = HashMultimap.create();

  public DAG()
  {
  }

  public boolean containsEdge(Edge<T> edge)
  {
    return srcToDests.containsEntry(edge.getSrc(), edge.getDest());
  }

  public boolean contains(T node)
  {
    return srcToDests.containsKey(node) || srcToDests.containsValue(node);
  }

  public Error addEdge(Edge<T> edge)
  {
    if(containsEdge(edge)) {
      return new Error();
    }

    if (hasPath(edge.getDest(), edge.getSrc())) {
      return new Error();
    }

    srcToDests.put(edge.getSrc(), edge.getDest());
    return null;
  }

  public boolean removeEdge(Edge<T> edge)
  {
    return srcToDests.remove(edge.getSrc(), edge.getDest());
  }

  public Set<T> getChildNodes(T node)
  {
    Preconditions.checkArgument(contains(node));

    return Sets.newHashSet(srcToDests.get(node));
  }

  public boolean hasIncomingEdges(T node)
  {
    return srcToDests.containsValue(node);
  }

  public Set<T> getRoots()
  {
    Set<T> roots = Sets.newHashSet();

    for (T node: srcToDests.keySet()) {
      if (!hasIncomingEdges(node)) {
        roots.add(node);
      }
    }

    return roots;
  }

  public List<T> inOrderTraversal(Comparator<T> comparator)
  {
    Preconditions.checkNotNull(comparator);

    DAG<T> clonedDag = (DAG<T>)clone();
    List<T> topologicalSort = Lists.newArrayList();
    Set<T> roots = getRoots();
    PriorityQueue<T> currentNodes = new PriorityQueue<T>(roots.size(), comparator);
    currentNodes.addAll(roots);

    while (!currentNodes.isEmpty()) {
      T currentNode = currentNodes.poll();
      topologicalSort.add(currentNode);

      Set<T> candidateNodes = clonedDag.getChildNodes(currentNode);

      for (T candidateNode: candidateNodes) {
        clonedDag.removeEdge(new Edge(currentNode, candidateNode));

        if (!clonedDag.hasIncomingEdges(candidateNode)) {
          currentNodes.add(candidateNode);
        }
      }
    }

    return topologicalSort;
  }

  @Override
  public Object clone()
  {
    DAG<T> dag = new DAG<T>();
    dag.srcToDests = srcToDests;
    return dag;
  }

  public boolean hasPath(T src, T dest)
  {
    if (!srcToDests.containsKey(src)) {
      return false;
    }

    if (!srcToDests.containsValue(dest)) {
      return false;
    }

    Set<T> nextNodes = srcToDests.get(src);

    for (T nextNode: nextNodes) {
      if (hasPath(nextNode, dest)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DAG<?> that = (DAG<?>) o;

    return srcToDests.equals(that.srcToDests);
  }

  @Override
  public int hashCode()
  {
    return srcToDests.hashCode();
  }

  public static class Error
  {
    private String message;

    public Error()
    {
    }

    public Error(String message)
    {
      this.message = Preconditions.checkNotNull(message);
    }

    public String getMessage()
    {
      return message;
    }
  }
}
