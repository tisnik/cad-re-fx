/**
 * @author Pavel Tišnovský
 */
package cz.tisnik.cadgfx.utils;

import java.util.*;

/**
 * Třída obsahující několik metod sloužících pro logování provozu informačního
 * systému do logovacího souboru. Obsah tohoto souboru je možné
 * přesměrovat na výstup terminálu, proto jsou také zapisovány i řídicí sekvence
 * terminálu kompatibilní s VT-100, které slouží k barevnému odlišení
 * jednotlivých typů (skupin) logovaných informací.
 *
 * @author Pavel Tišnovský
 */
public class Log
{

  /**
   * Asociativní pole, do níž jsou uloženy jednotlivé barvy terminálu.
   */
  static HashMap<String, String> colors = new HashMap<String, String>( 10 );

  /**
   * Inicializace asociativního pole colors.
   */
  static
  {
    colors.put( "RED", "31" );
    colors.put( "GREEN", "32" );
    colors.put( "ORANGE", "33" );
    colors.put( "BLUE", "34" );
    colors.put( "VIOLET", "35" );
    colors.put( "MAGENTA", "35" );
    colors.put( "LIGHT_BLUE", "36" );
    colors.put( "GRAY", "38" );
    colors.put( "GREY", "38" );
  }

  /**
   * Aktuální hodnota odsazení.
   */
  private int indent = 0;

  /**
   * Počet mezer, kterými se výpis odsadí v každé úrovni.
   */
  private int indentDelta = 2;

  /**
   * Jméno třídy, ve které se tento objekt nachází.
   */
  private String className = null;

  /**
   * Implicitní konstruktor.
   */
  public Log()
  {
    this.className = "unknown";
  }

  /**
   * Explicitní konstruktor, ve kterém se předává jméno třídy, která logování
   * provádí.
   */
  public Log(String className)
  {
    this.className = className;
  }

  /**
   * Pomocná metoda pro vytvoření řetězce s aktuálním datem a časem. Formát
   * vytvořeného řetězce je: "27 Mar 2007 10:20:30" Pozor: nevrací se typ
   * String, ale StringBuffer, což umožňuje rychlé spojování vygenerovaných
   * řetězců.
   */
  @SuppressWarnings("deprecation")
  private String getDate()
  {
    // vytvorime pomocny buffer, do ktereho seskladame retezec
    Date date = new Date();
    return String.format( "%04d-%02d-%02d  %02d:%02d:%02d", 1900 + date.getYear(), date.getMonth(),
        date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds() );
  }

  /**
   * Výpis logovacích informací; základní verze metody.
   *
   * @param str řetězec, který bude vypsán do logovacího souboru
   */
  public void log(String str)
  {
    this.log( this.className, str );
  }

  /**
   * Výpis logovacích informací na standardní výstup, který je posléze JBossem
   * přesměrován do logovacího souboru.
   */
  public void log(String className, String str)
  {
    StringBuffer spaces = new StringBuffer();
    for ( int i = 0; i < this.indent; i++ )
    {
      spaces.append( ' ' );
    }

    StringBuffer output = new StringBuffer();
    str = "$GREEN$cadgfx  $LIGHT_BLUE$" + this.getDate() + "  $ORANGE$" + String.format("%-40s", className) + spaces
        + "  $GRAY$" + str;

    String[] pieces = str.split( "\\$" );

    for ( String piece : pieces )
    {
      if ( colors.containsKey( piece.toUpperCase() ) )
      {
        output.append( "\033[0;" + colors.get( piece.toUpperCase() ) + "m" );
      }
      else
      {
        output.append( piece );
      }
    }
    // vypis vytvoreneho retezce na standardni vystup
    System.out.println( output.append( "\033[0m" ) );
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě výpisu hodnot
   * proměnných. Neinicializované hodnoty jsou vybarveny červeně.
   *
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   * @param o hodnotový objekt
   */
  public void logSet(String str, Object o)
  {
    String value = o == null ? "$RED$null" : "$GRAY$" + o.toString();
    this.log( "$LIGHT_BLUE$" + str + "$ORANGE$ = " + value );
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě výpisu hodnot
   * proměnných. Neinicializované hodnoty jsou vybarveny červeně.
   *
   * @param className jméno třídy, která logování provádí
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   * @param o hodnotový objekt
   */
  public void logSet(String className, String str, Object o)
  {
    String value = o == null ? "$RED$null" : "$GRAY$" + o.toString();
    this.log( className, "$LIGHT_BLUE$" + str + "$ORANGE$ = " + value );
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě vstupu
   * do bloku kódu.
   *
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   */
  public void logBegin(String str)
  {
    this.logBegin( this.className, str );
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě vstupu
   * do bloku kódu.
   *
   * @param className jméno třídy, která logování provádí
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   */
  public void logBegin(String className, String str)
  {
    this.log( className, str + " $GREEN$begin" );
    this.indent += this.indentDelta;
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě výstupu
   * z bloku kódu.
   *
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   */
  public void logEnd(String str)
  {
    this.logEnd( this.className, str );
  }

  /**
   * Výpis logovacích informací; verze metody použitá v případě výstupu
   * z bloku kódu.
   *
   * @param className jméno třídy, která logování provádí
   * @param str
   *          řetězec, který bude vypsán do logovacího souboru
   */
  public void logEnd(String className, String str)
  {
    if ( this.indent >= this.indentDelta )
    {
      this.indent -= this.indentDelta;
    }
    this.log( className, str + " $VIOLET$end" );
  }

  public void logError(String str)
  {
    this.logError( this.className, str );
  }

  public void logError(String className, String str)
  {
    this.log( className, "$RED$error $GRAY$" + str );
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



// -----------------------------------------------------------------------------
// finito
// -----------------------------------------------------------------------------

// vim: foldmethod=marker
