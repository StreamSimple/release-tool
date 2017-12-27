/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsimple.rt.dag;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.streamsimple.guava.common.base.Preconditions;
import com.streamsimple.guava.common.collect.HashMultimap;
import com.streamsimple.guava.common.collect.Lists;
import com.streamsimple.guava.common.collect.SetMultimap;
import com.streamsimple.guava.common.collect.Sets;
import com.streamsimple.javautil.err.ReturnError;
import com.streamsimple.javautil.err.ReturnErrorImpl;

public class DAG<T> implements Cloneable
{
  private Set<T> nodes = Sets.newHashSet();
  private SetMultimap<T, T> srcToDests = HashMultimap.create();

  public DAG()
  {
  }

  public boolean contains(Edge<T> edge)
  {
    return srcToDests.containsEntry(edge.getSrc(), edge.getDest());
  }

  public boolean contains(T node)
  {
    return nodes.contains(node);
  }

  public ReturnError addEdge(Edge<T> edge)
  {
    if (contains(edge)) {
      return new ReturnErrorImpl("Contains edge: " + edge);
    }

    if (hasPath(edge.getDest(), edge.getSrc())) {
      return new ReturnErrorImpl("Has path from " + edge.getDest() + " to " + edge.getSrc());
    }

    nodes.add(edge.getSrc());
    nodes.add(edge.getDest());
    srcToDests.put(edge.getSrc(), edge.getDest());
    return null;
  }

  public boolean removeEdge(Edge<T> edge)
  {
    return srcToDests.remove(edge.getSrc(), edge.getDest());
  }

  public boolean removeNode(T node)
  {
    if (!nodes.contains(node)) {
      return false;
    }

    nodes.remove(node);
    Set<T> dests = Sets.newHashSet(srcToDests.get(node));

    for (T dest: dests) {
      srcToDests.remove(node, dest);
    }

    return true;
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

    nodes.forEach(node -> {
      if (!hasIncomingEdges(node)) {
        roots.add(node);
      }
    });

    return roots;
  }

  public Set<Edge<T>> getEdges()
  {
    Set<Edge<T>> edgeSet = Sets.newHashSet();

    srcToDests.keySet().forEach(src -> {
      srcToDests.get(src).forEach(dest -> {
        edgeSet.add(new Edge<>(src, dest));
      });
    });

    return edgeSet;
  }

  public Set<T> getNodes()
  {
    return Sets.newHashSet(nodes);
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

      clonedDag.removeNode(currentNode);
    }

    return topologicalSort;
  }

  @Override
  public Object clone()
  {
    DAG<T> dag = new DAG<T>();
    dag.srcToDests = HashMultimap.create();

    srcToDests.keySet().forEach(key -> {
      srcToDests.get(key).forEach(value -> {
        dag.srcToDests.put(key, value);
      });
    });

    dag.nodes = Sets.newHashSet(nodes);
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

    if (nextNodes.contains(dest)) {
      return true;
    }

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
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DAG<?> that = (DAG<?>)o;

    return srcToDests.equals(that.srcToDests);
  }

  @Override
  public int hashCode()
  {
    return srcToDests.hashCode();
  }
}
