// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio.store.state.jdbc.hsqldb;

public interface HSQLDBQueries {
  final static String SQL_STATE_READ =
          "SELECT TBL_{0}.S_TYPE, TBL_{0}.S_TYPE_VERSION, TBL_{0}.S_DATA, TBL_{0}.S_DATA_VERSION, TBL_{0}.S_METADATA_VALUE, TBL_{0}.S_METADATA_OP " +
          "FROM TBL_{0} " +
          "WHERE TBL_{0}.S_ID = ?";

  final static String SQL_STATE_WRITE =
          "MERGE INTO TBL_{0} \n" +
          "USING (VALUES ?, ?, ?, {1}, ?, ?, ?) \n" +
          "S (S_ID, S_TYPE, S_TYPE_VERSION, S_DATA, S_DATA_VERSION, S_METADATA_OP, S_METADATA_VALUE) \n" +
          "ON (TBL_{0}.S_ID = S.S_ID) \n" +
          "WHEN MATCHED THEN UPDATE \n" +
                  "SET TBL_{0}.S_TYPE = S.S_TYPE, \n" +
                  "    TBL_{0}.S_TYPE_VERSION = S.S_TYPE_VERSION, \n" +
                  "    TBL_{0}.S_DATA = S.S_DATA, \n" +
                  "    TBL_{0}.S_DATA_VERSION = S.S_DATA_VERSION, \n" +
                  "    TBL_{0}.S_METADATA_OP = S.S_METADATA_OP, \n" +
                  "    TBL_{0}.S_METADATA_VALUE = S.S_METADATA_VALUE \n" +
          "WHEN NOT MATCHED THEN INSERT \n" +
                  "(S_ID, S_TYPE, S_TYPE_VERSION, S_DATA, S_DATA_VERSION, S_METADATA_OP, S_METADATA_VALUE) \n" +
                  "VALUES (S.S_ID, S.S_TYPE, S.S_TYPE_VERSION, S.S_DATA, S.S_DATA_VERSION, S.S_METADATA_VALUE, S.S_METADATA_OP)";

  final static String SQL_FORMAT_BINARY_CAST = "CAST(? AS VARBINARY(65535))";
  final static String SQL_FORMAT_TEXT_CAST = "CAST(? AS LONGVARCHAR(65535))";

  final static String TBL_VLINGO_SYMBIO_DISPATCHABLES = "TBL_VLINGO_SYMBIO_DISPATCHABLES";

  final static String SQL_CREATE_DISPATCHABLES_STORE =
          "CREATE TABLE {0} (\n" +
          "   D_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
          "   D_CREATED_AT TIMESTAMP NOT NULL," +
          "   D_ORIGINATOR_ID VARCHAR(32) NOT NULL," +
          "   D_DISPATCH_ID VARCHAR(128) NOT NULL,\n" +
          "   D_STATE_ID VARCHAR(128) NOT NULL, \n" +
          "   D_STATE_TYPE VARCHAR(256) NOT NULL,\n" +
          "   D_STATE_TYPE_VERSION INT NOT NULL,\n" +
          "   D_STATE_DATA {1} NOT NULL,\n" +
          "   D_STATE_DATA_VERSION INT NOT NULL,\n" +
          "   D_STATE_METADATA_VALUE VARCHAR(4000) NOT NULL,\n" +
          "   D_STATE_METADATA_OP VARCHAR(128) NOT NULL,\n" +
          "   D_STATE_METADATA_OBJECT LONGVARCHAR(65535),\n" +
          "   D_STATE_METADATA_OBJECT_TYPE VARCHAR(256)\n" +
          ");";

  final static String SQL_DISPATCH_ID_INDEX =
          "CREATE INDEX IDX_DISPATCHABLES_DISPATCH_ID \n" +
          "ON {0} (D_DISPATCH_ID);";

  final static String SQL_ORIGINATOR_ID_INDEX =
          "CREATE INDEX IDX_DISPATCHABLES_ORIGINATOR_ID \n" +
          "ON {0} (D_ORIGINATOR_ID);";

  final static String SQL_CREATE_STATE_STORE =
          "CREATE TABLE TBL_{0} (\n" +
          "   S_ID VARCHAR(128) NOT NULL,\n" +
          "   S_TYPE VARCHAR(256) NOT NULL,\n" +
          "   S_TYPE_VERSION INT NOT NULL,\n" +
          "   S_DATA {1} NOT NULL,\n" +
          "   S_DATA_VERSION INT NOT NULL,\n" +
          "   S_METADATA_VALUE VARCHAR(4000) NOT NULL,\n" +
          "   S_METADATA_OP VARCHAR(128) NOT NULL,\n" +
          "   PRIMARY KEY (S_ID) \n" +
          ");";

  final static String SQL_FORMAT_TEXT = "LONGVARCHAR(65535)";
  final static String SQL_FORMAT_BINARY = "VARBINARY(65535)";

  final static String SQL_DISPATCHABLE_APPEND =
          "INSERT INTO {0} \n" +
               "(D_ID, D_CREATED_AT, D_ORIGINATOR_ID, D_DISPATCH_ID, \n" +
               " D_STATE_ID, D_STATE_TYPE, D_STATE_TYPE_VERSION, \n" +
               " D_STATE_DATA, D_STATE_DATA_VERSION, \n" +
               " D_STATE_METADATA_VALUE, D_STATE_METADATA_OP, D_STATE_METADATA_OBJECT, D_STATE_METADATA_OBJECT_TYPE) \n" +
               "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  final static String SQL_DISPATCHABLE_DELETE =
          "DELETE FROM {0} WHERE D_DISPATCH_ID = ?";

  final static String SQL_DISPATCHABLE_SELECT =
          "SELECT D_CREATED_AT, D_DISPATCH_ID, D_STATE_ID, D_STATE_TYPE, D_STATE_TYPE_VERSION, D_STATE_DATA, D_STATE_DATA_VERSION, \n" +
          "       D_STATE_METADATA_VALUE, D_STATE_METADATA_OP, D_STATE_METADATA_OBJECT, D_STATE_METADATA_OBJECT_TYPE \n" +
          "FROM {0} \n" +
          "WHERE D_ORIGINATOR_ID = ? ORDER BY D_ID";

  final static String SQL_CREATE_ENTRY_STORE =
          "CREATE TABLE {0} (\n" +
          "   E_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
          "   E_TYPE VARCHAR(256) NOT NULL,\n" +
          "   E_TYPE_VERSION INT NOT NULL,\n" +
          "   E_DATA {1} NOT NULL,\n" +
          "   E_METADATA_VALUE VARCHAR(4000) NULL,\n" +
          "   E_METADATA_OP VARCHAR(128) NULL\n" +
          ");";

  final static String TBL_VLINGO_SYMBIO_STATE_ENTRY = "TBL_VLINGO_SYMBIO_STATE_ENTRY";

  final static String SQL_APPEND_ENTRY =
          "INSERT INTO {0} \n" +
               "(E_TYPE, E_TYPE_VERSION, E_DATA, E_METADATA_VALUE, E_METADATA_OP) \n" +
               "VALUES (?, ?, ?, ?, ?)";

  final static String SQL_APPEND_ENTRY_IDENTITY = "CALL IDENTITY()";

  final static String SQL_QUERY_ENTRY_BATCH =
          "SELECT E_ID, E_TYPE, E_TYPE_VERSION, E_DATA, E_METADATA_VALUE, E_METADATA_OP FROM " +
                  TBL_VLINGO_SYMBIO_STATE_ENTRY + " WHERE E_ID >= ? " +
                  "ORDER BY E_ID LIMIT ?";

  final static String SQL_QUERY_ENTRY =
          "SELECT E_ID, E_TYPE, E_TYPE_VERSION, E_DATA, E_METADATA_VALUE, E_METADATA_OP FROM " +
                  TBL_VLINGO_SYMBIO_STATE_ENTRY + " WHERE E_ID = ? ";
}
