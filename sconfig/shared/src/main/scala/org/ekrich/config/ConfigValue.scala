/**
 * Copyright (C) 2011-2012 Typesafe Inc. <http://typesafe.com>
 */
package org.ekrich.config

/**
 * An immutable value, following the <a href="http://json.org">JSON</a> type
 * schema.
 *
 * <p> Because this object is immutable, it is safe to use from multiple threads
 * and there's no need for "defensive copies."
 *
 * <p> <em>Do not implement interface {@code ConfigValue}</em>; it should only
 * be implemented by the config library. Arbitrary implementations will not work
 * because the library internals assume a specific concrete implementation.
 * Also, this interface is likely to grow new methods over time, so third-party
 * implementations will break.
 */
trait ConfigValue extends ConfigMergeable {

  /**
   * The origin of the value (file, line number, etc.), for debugging and error
   * messages.
   *
   * @return
   *   where the value came from
   */
  def origin: ConfigOrigin

  /**
   * The {@link ConfigValueType} of the value; matches the JSON type schema.
   *
   * @return
   *   value's type
   */
  def valueType: ConfigValueType

  /**
   * Returns the value as a plain Java boxed value, that is, a {@code String},
   * {@code Number}, {@code Boolean}, {@code Map<String,Object>}, {@code
   * List<Object>}, or {@code null}, matching the {@link #valueType} of this
   * {@code ConfigValue}. If the value is a {@link ConfigObject} or
   * [[ConfigList]], it is recursively unwrapped.
   *
   * @return
   *   a plain Java value corresponding to this ConfigValue
   */
  def unwrapped: AnyRef

  /**
   * Renders the config value as a HOCON string. This method is primarily
   * intended for debugging, so it tries to add helpful comments and whitespace.
   *
   * <p> If the config value has not been resolved (see
   * [[Config!.resolve()* Config.resolve()]]), it's possible that it can't be
   * rendered as valid HOCON. In that case the rendering should still be useful
   * for debugging but you might not be able to parse it. If the value has been
   * resolved, it will always be parseable.
   *
   * <p> This method is equivalent to {@code
   * render(ConfigRenderOptions.defaults())}.
   *
   * @return
   *   the rendered value
   */
  def render: String

  /**
   * Renders the config value to a string, using the provided options.
   *
   * <p> If the config value has not been resolved (see
   * [[[Config!.resolve()* Config.resolve()]]), it's possible that it can't be
   * rendered as valid HOCON. In that case the rendering should still be useful
   * for debugging but you might not be able to parse it. If the value has been
   * resolved, it will always be parseable.
   *
   * <p> If the config value has been resolved and the options disable all
   * HOCON-specific features (such as comments), the rendering will be valid
   * JSON. If you enable HOCON-only features such as comments, the rendering
   * will not be valid JSON.
   *
   * @param options
   *   the rendering options
   * @return
   *   the rendered value
   */
  def render(options: ConfigRenderOptions): String

  override def withFallback(other: ConfigMergeable): ConfigValue

  /**
   * Places the value inside a {@link Config} at the given path. See also
   * [[ConfigValue#atKey]].
   *
   * @param path
   *   path to store this value at.
   * @return
   *   a {@code Config} instance containing this value at the given path.
   */
  def atPath(path: String): Config

  /**
   * Places the value inside a {@link Config} at the given key. See also
   * [[ConfigValue#atPath]].
   *
   * @param key
   *   key to store this value at.
   * @return
   *   a {@code Config} instance containing this value at the given key.
   */
  def atKey(key: String): Config

  /**
   * Returns a {@code ConfigValue} based on this one, but with the given origin.
   * This is useful when you are parsing a new format of file or setting
   * comments for a single ConfigValue.
   *
   * @since 1.3.0
   * @param origin
   *   the origin set on the returned value
   * @return
   *   the new ConfigValue with the given origin
   */
  def withOrigin(origin: ConfigOrigin): ConfigValue
}
