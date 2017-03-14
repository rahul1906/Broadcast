package com.hm.connector

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by rahul on 13/3/17.
  */
object MysqlClient {

  private val dbc = "jdbc:mysql://" + "127.0.0.1" + ":" + 3306 + "/" + "distributed" + "?user=" + "root" + "&password=" + "root"
  classOf[com.mysql.jdbc.Driver]
  private var conn: Connection = DriverManager.getConnection(dbc)

  def getConnection: Connection = {
    if (conn.isClosed) {
      conn = DriverManager.getConnection(dbc)
    }
    conn
  }
  val autoIncValuesForTable: Map[String, Array[String]] = Map(
    "grp" -> Array("id")

  )

  def closeConnection() = conn.close()

  def executeQuery(query: String): Boolean = {
    val statement = getConnection.createStatement()
    try
      statement.execute(query)
    finally statement.close()
  }

  def getResultSet(query: String): ResultSet={
    val statement=getConnection.createStatement()
    statement.executeQuery(query)
  }

  def insert(tableName: String, elements: Map[String, Any]): Int = {
    try {
      val colNames: ArrayBuffer[String] = ArrayBuffer()
      val values: ArrayBuffer[Any] = ArrayBuffer()
      elements.foreach(i => {
        colNames += i._1
        values += i._2
      })

      val insertQuery = "INSERT INTO " + tableName + " (" + colNames.mkString(",") + ") VALUES (" + colNames.indices.map(i => "?").mkString(",") + ")"

      val returnColumns: Array[String] = autoIncValuesForTable.getOrElse(tableName, Array())
      val preparedStatement: PreparedStatement = getConnection.prepareStatement(insertQuery, returnColumns)

      values.zipWithIndex.foreach(i => addToPreparedStatement(i._1, i._2 + 1, preparedStatement))
      var generatedId: Int = 0
      try {

        preparedStatement.executeUpdate()
        if (returnColumns.nonEmpty) {
          val gkSet = preparedStatement.getGeneratedKeys
          if (gkSet.next()) {
            generatedId = gkSet.getInt(1)
          }
        }
      }
      finally preparedStatement.close()

      generatedId
    } catch {
      case e: Exception => e.printStackTrace()
        0
    }
  }
  private def addToPreparedStatement(value: Any, index: Int, preparedStatement: PreparedStatement) = {
    value match {
      case v: Long => preparedStatement.setLong(index, v)
      case v: Int => preparedStatement.setInt(index, v)
      case v: Double => preparedStatement.setDouble(index, v)
      case v: String => preparedStatement.setString(index, v)

      case v: Array[Byte] => preparedStatement.setBytes(index, v)
      case v: Serializable => preparedStatement.setObject(index, v)
      case _ => preparedStatement.setString(index, value.toString)
    }
  }


}
