/**
 * @author Pavel Tišnovský
 */
package cz.tisnik.cadgfx.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import cz.tisnik.cadgfx.utils.Log;

/**
 * Objekt, který je vytvořený ve chvíli inicializace či naopak zrušení sezení
 * (session) pro některého z uživatelů aplikace.
 *
 * @author Pavel Tišnovský
 */
public class SessionListener implements HttpSessionListener
{

  /**
   * Instance objektu použitého pro logování do logovacího souboru či na
   * standardní výstup servlet kontejneru.
   */
  private Log log = new Log( this.getClass().getName() );

  /**
   * Tato metoda je zavolána ihned po vytvoření sezení pro uživatele webové
   * aplikace.
   */
  public void sessionCreated(HttpSessionEvent sessionEvent)
  {
    this.log.log( "session created " + sessionEvent.getSession().getId() );
  }

  /**
   * Tato metoda je zavolána těsně před zrušením sezení pro uživatele webové
   * aplikace.
   */
  public void sessionDestroyed(HttpSessionEvent sessionEvent)
  {
    this.log.log( "session destroyed " + sessionEvent.getSession().getId() );
  }

  /**
   * Funkce, kterou lze použít pro otestování základní funkčnosti chování tohoto
   * objektu.
   *
   * @param args nepoužito
   */
  public static void main(String[] args)
  {
    System.out.println( "test start" );
    System.out.println( "test end" );
  }

}



//-----------------------------------------------------------------------------
//finito
//-----------------------------------------------------------------------------

//vim: foldmethod=marker
