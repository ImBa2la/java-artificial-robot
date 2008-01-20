/**
 * <p>Title: interface Reloadable</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Yandex</p>
 * @author Oleg Okhotnikov
 * @version 1.0
 */

package ru.yandex.utils;

public interface Reloadable
{
  /**
   * Method to reload by SIGHUP signal handler
   */
  public void reload ();
}
