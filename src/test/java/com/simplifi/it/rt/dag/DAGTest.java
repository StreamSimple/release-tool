package com.simplifi.it.rt.dag;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Created by tfarkas on 5/28/17.
 */
public class DAGTest
{
  @Test
  public void containsNodeTest()
  {
    DAG<String> dag = buildTestDAG1();

    Assert.assertTrue(dag.contains("1"));
    Assert.assertFalse(dag.contains("10"));
  }

  @Test
  public void containsEdgeTest()
  {
    DAG<String> dag = buildTestDAG1();

    Assert.assertTrue(dag.contains(new Edge<>("1", "3")));
    Assert.assertFalse(dag.contains(new Edge<>("1", "6")));
    Assert.assertFalse(dag.contains(new Edge<>("1", "10")));
  }

  @Test
  public void getEdgesTest()
  {
    DAG<String> dag = buildTestDAG1();

    Set<Edge<String>> actualEdges = dag.getEdges();
    Set<Edge<String>> expectedEdges = Sets.newHashSet(
      new Edge<>("1", "2"),
      new Edge<>("1", "3"),
      new Edge<>("2", "4"),
      new Edge<>("2", "5"),
      new Edge<>("5", "6"),
      new Edge<>("3", "6")
    );

    Assert.assertEquals(actualEdges, expectedEdges);
  }

  @Test
  public void simpleGetRootsTest()
  {
    DAG<String> dag = buildTestDAG1();

    Set<String> expectedRoots = Sets.newHashSet("1");

    Assert.assertEquals(expectedRoots, dag.getRoots());
  }

  @Test
  public void simpleRemoveNodeTest()
  {
    DAG<String> dag = buildTestDAG1();

    Assert.assertTrue(dag.removeNode("1"));
    Assert.assertFalse(dag.removeNode("1"));
    Assert.assertFalse(dag.removeNode("10"));
    Assert.assertFalse(dag.contains("1"));
    Assert.assertFalse(dag.contains(new Edge("1", "2")));
    Assert.assertFalse(dag.contains(new Edge("1", "3")));
  }

  @Test
  public void simpleInOrderTraversalTest()
  {

  }

  @Test
  public void removeEdgeTest()
  {
    DAG<String> dag = buildTestDAG1();

    Assert.assertTrue(dag.contains(new Edge<>("1", "3")));
    Assert.assertTrue(dag.removeEdge(new Edge<>("1", "3")));
    Assert.assertFalse(dag.contains(new Edge<>("1", "3")));

    Set<Edge<String>> actualEdges = dag.getEdges();
    Set<Edge<String>> expectedEdges = Sets.newHashSet(
      new Edge<>("1", "2"),
      new Edge<>("2", "4"),
      new Edge<>("2", "5"),
      new Edge<>("5", "6"),
      new Edge<>("3", "6")
    );

    Assert.assertEquals(actualEdges, expectedEdges);
  }

  /*
   *               1
   *              / \
   *             /  \
   *            2   3
   *           / \  |
   *          4  5  |
   *              \ |
   *               6
   *
   */
  private DAG<String> buildTestDAG1()
  {
    DAG<String> dag = new DAG<>();

    DAG.Error error = dag.addEdge(new Edge<>("1", "2"));
    Assert.assertNull(error);
    error = dag.addEdge(new Edge<>("1", "3"));
    Assert.assertNull(error);
    error = dag.addEdge(new Edge<>("2", "4"));
    Assert.assertNull(error);
    error = dag.addEdge(new Edge<>("2", "5"));
    Assert.assertNull(error);
    error = dag.addEdge(new Edge<>("5", "6"));
    Assert.assertNull(error);
    error = dag.addEdge(new Edge<>("3", "6"));
    Assert.assertNull(error);

    return dag;
  }
}
