/**
 * Copyright (C) 2011-2012 Typesafe Inc. <http://typesafe.com>
 */
package org.ekrich.config

/**
 * An opaque handle to something that can be parsed, obtained from
 * [[ConfigIncludeContext]].
 *
 * <p> <em>Do not implement this interface</em>; it should only be implemented
 * by the config library. Arbitrary implementations will not work because the
 * library internals assume a specific concrete implementation. Also, this
 * interface is likely to grow new methods over time, so third-party
 * implementations will break.
 */
trait ConfigParseable {

  /**
   * Parse whatever it is. The options should come from
   * [[ConfigParseable#options options()]] but you could tweak them if you like.
   *
   * @param options
   *   parse options, should be based on the ones from
   *   [[ConfigParseable#options options()]]
   * @return
   *   the parsed object
   */
  def parse(options: ConfigParseOptions): ConfigObject

  /**
   * Returns a {@link ConfigOrigin} describing the origin of the parseable item.
   *
   * @return
   *   the origin of the parseable item
   */
  def origin(): ConfigOrigin

  /**
   * Get the initial options, which can be modified then passed to parse().
   * These options will have the right description, includer, and other
   * parameters already set up.
   *
   * @return
   *   the initial options
   */
  def options(): ConfigParseOptions
}
