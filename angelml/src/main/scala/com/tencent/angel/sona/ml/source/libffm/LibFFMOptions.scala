/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.tencent.angel.sona.ml.source.libffm

import org.apache.spark.sql.util.SONACaseInsensitiveMap

/**
  * Options for the LibFFM data source.
  */
private[libffm] class LibFFMOptions(@transient private val parameters: SONACaseInsensitiveMap[String])
  extends Serializable {

  import LibFFMOptions._

  def this(parameters: Map[String, String]) = this(SONACaseInsensitiveMap(parameters))

  /**
    * Number of features. If unspecified or nonpositive, the number of features will be determined
    * automatically at the cost of one additional pass.
    */
  val numFeatures: Option[Long] = parameters.get(NUM_FEATURES).map(_.toLong).filter(_ > 0)

  val numFields: Option[Int] = parameters.get(NUM_FIELDS).map(_.toInt).filter(_ > 0)

  val isSparse: Boolean = parameters.getOrElse(VECTOR_TYPE, SPARSE_VECTOR_TYPE) match {
    case SPARSE_VECTOR_TYPE => true
    case DENSE_VECTOR_TYPE => false
    case o => throw new IllegalArgumentException(s"Invalid value `$o` for parameter " +
      s"`$VECTOR_TYPE`. Expected types are `sparse` and `dense`.")
  }

  val isLongKey: Boolean = parameters.getOrElse(KEY_TYPE, INT_KEY_TYPE) match {
    case LONG_KEY_TYPE => true
    case INT_KEY_TYPE => false
    case o => throw new IllegalArgumentException(s"Invalid value `$o` for parameter " +
      s"`$KEY_TYPE`. Expected types are `int` and `long`.")
  }
}

private[libffm] object LibFFMOptions {
  val NUM_FEATURES = "numFeatures"
  val NUM_FIELDS = "numFields"
  val VECTOR_TYPE = "vectorType"
  val DENSE_VECTOR_TYPE = "dense"
  val SPARSE_VECTOR_TYPE = "sparse"
  val KEY_TYPE = "keyType"
  val INT_KEY_TYPE = "int"
  val LONG_KEY_TYPE = "long"
}