package com.simplifi.it.rt.release;

/**
 * Created by tfarkas on 5/27/17.
 */
public class RepoConfig
{
  private String path;

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoConfig that = (RepoConfig) o;

    return path.equals(that.path);
  }

  @Override
  public int hashCode() {
    return path.hashCode();
  }
}
