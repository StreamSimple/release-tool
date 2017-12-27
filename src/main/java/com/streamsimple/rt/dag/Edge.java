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

import com.streamsimple.guava.common.base.Preconditions;

public class Edge<T>
{
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
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Edge<?> edge = (Edge<?>)o;

    if (!src.equals(edge.src)) {
      return false;
    }

    return dest.equals(edge.dest);
  }

  @Override
  public int hashCode()
  {
    int result = src.hashCode();
    result = 31 * result + dest.hashCode();
    return result;
  }
}
