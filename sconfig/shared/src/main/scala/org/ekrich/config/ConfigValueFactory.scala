/**
 * Copyright (C) 2011-2012 Typesafe Inc. <http://typesafe.com>
 */
package org.ekrich.config

import java.{util => ju}
import java.{lang => jl}
import org.ekrich.config.impl.ConfigImpl

/**
 * This class holds some static factory methods for building {@link ConfigValue}
 * instances. See also {@link ConfigFactory} which has methods for parsing files
 * and certain in-memory data structures.
 */
object ConfigValueFactory {

  /**
   * Creates a {@link ConfigValue} from a plain Java boxed value, which may be a
   * <code>Boolean</code>, <code>Number</code>, <code>String</code>,
   * <code>Map</code>, <code>Iterable</code>, or <code>null</code>. A
   * <code>Map</code> must be a <code>Map</code> from String to more values that
   * can be supplied to <code>fromAnyRef()</code>. An <code>Iterable</code> must
   * iterate over more values that can be supplied to <code>fromAnyRef()</code>.
   * A <code>Map</code> will become a {@link ConfigObject} and an
   * <code>Iterable</code> will become a {@link ConfigList}. If the
   * <code>Iterable</code> is not an ordered collection, results could be
   * strange, since <code>ConfigList</code> is ordered.
   *
   * <p> In a <code>Map</code> passed to <code>fromAnyRef()</code>, the map's
   * keys are plain keys, not path expressions. So if your <code>Map</code> has
   * a key "foo.bar" then you will get one object with a key called "foo.bar",
   * rather than an object with a key "foo" containing another object with a key
   * "bar".
   *
   * <p> The originDescription will be used to set the `origin()` field on the
   * `ConfigValue`. It should normally be the name of the file the values came
   * from, or something short describing the value such as "default settings".
   * The originDescription is prefixed to error messages so users can tell where
   * problematic values are coming from.
   *
   * <p> Supplying the result of `ConfigValue.unwrapped()` to this function is
   * guaranteed to work and should give you back a `ConfigValue` that matches
   * the one you unwrapped. The re-wrapped `ConfigValue` will lose some
   * information that was present in the original such as its origin, but it
   * will have matching values.
   *
   * <p> If you pass in a <code>ConfigValue</code> to this function, it will be
   * returned unmodified. (The <code>originDescription</code> will be ignored in
   * this case.)
   *
   * <p> This function throws if you supply a value that cannot be converted to
   * a ConfigValue, but supplying such a value is a bug in your program, so you
   * should never handle the exception. Just fix your program (or report a bug
   * against this library).
   *
   * @param object
   *   object to convert to ConfigValue
   * @param originDescription
   *   name of origin file or brief description of what the value is
   * @return
   *   a new value
   */
  def fromAnyRef(obj: Object, originDescription: String): ConfigValue =
    ConfigImpl.fromAnyRef(obj, originDescription)

  /**
   * See the
   * [[#fromAnyRef(obj:Object,originDescription:String)* fromAnyRef(Object,String)]]
   * documentation for details. This is a typesafe wrapper that only works on
   * `java.util.Map` and returns {@link ConfigObject} rather than
   * [[ConfigValue]].
   *
   * <p> If your <code>Map</code> has a key "foo.bar" then you will get one
   * object with a key called "foo.bar", rather than an object with a key "foo"
   * containing another object with a key "bar". The keys in the map are keys;
   * not path expressions. That is, the <code>Map</code> corresponds exactly to
   * a single {@code ConfigObject}. The keys will not be parsed or modified, and
   * the values are wrapped in ConfigValue. To get nested {@code ConfigObject},
   * some of the values in the map would have to be more maps.
   *
   * <p> See also
   * [[ConfigFactory$.parseMap(values:java\.util\.Map[String,_],originDescription:String)* ConfigFactory.parseMap(Map,String)]]
   * which interprets the keys in the map as path expressions.
   *
   * @param values
   *   map from keys to plain Java values
   * @param originDescription
   *   description to use in {@link ConfigOrigin} of created values
   * @return
   *   a new {@link ConfigObject} value
   */
  def fromMap(
      values: ju.Map[String, _],
      originDescription: String
  ): ConfigObject =
    fromAnyRef(values, originDescription).asInstanceOf[ConfigObject]

  /**
   * See the
   * [[#fromAnyRef(obj:Object,originDescription:String)* fromAnyRef(Object,String)]]
   * documentation for details. This is a typesafe wrapper that only works on
   * `java.lang.Iterable` and returns {@link ConfigList} rather than
   * [[ConfigValue]].
   *
   * @param values
   *   list of plain Java values
   * @param originDescription
   *   description to use in {@link ConfigOrigin} of created values
   * @return
   *   a new {@link ConfigList} value
   */
  def fromIterable(
      values: jl.Iterable[_],
      originDescription: String
  ): ConfigList =
    fromAnyRef(values, originDescription).asInstanceOf[ConfigList]

  /**
   * See the other overload
   * [[#fromAnyRef(obj:Object,originDescription:String)* fromAnyRef(Object,String)]]
   * for details, this one just uses a default origin description.
   *
   * @param object
   *   a plain Java value
   * @return
   *   a new {@link ConfigValue}
   */
  def fromAnyRef(obj: Object): ConfigValue = fromAnyRef(obj, null)

  /**
   * See the other overload
   * [[#fromMap(values:java\.util\.Map[String,_],originDescription:String)* fromMap(Map,String)]]
   * for details, this one just uses a default origin description.
   *
   * <p> See also
   * [[ConfigFactory$.parseMap(values:java\.util\.Map[String,_])* ConfigFactory.parseMap(ju.Map)]]
   * which interprets the keys in the map as path expressions.
   *
   * @param values
   *   map from keys to plain Java values
   * @return
   *   a new {@link ConfigObject}
   */
  def fromMap(values: ju.Map[String, _]): ConfigObject = fromMap(values, null)

  /**
   * See the other overload of
   * [[#fromIterable(values:Iterable[_],originDescription:String)* fromIterable(jl.Iterable, String)]]
   * for details, this one just uses a default origin description.
   *
   * @param values
   *   list of plain Java values
   * @return
   *   a new {@link ConfigList}
   */
  def fromIterable(values: jl.Iterable[_]): ConfigList =
    fromIterable(values, null)
}

final class ConfigValueFactory private () {}
