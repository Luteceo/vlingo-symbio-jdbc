// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio.store.state.jdbc;

import io.vlingo.actors.Logger;
import io.vlingo.actors.World;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.state.Entity1;
import io.vlingo.symbio.store.state.StateTypeStateStoreMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public abstract class JDBCStorageDelegateTest {
    private Configuration.TestConfiguration configuration;
    private JDBCStorageDelegate<Object> delegate;
    private String entity1StoreName;
    private World world;

    @Test
    public void testThatDatabaseOpensTablesCreated() throws Exception {
        configuration = testConfiguration(DataFormat.Text);
        delegate = storageDelegate(configuration, world.defaultLogger());

        assertNotNull(delegate);
    }

    @Test
    public void testThatTextWritesRead() throws Exception {
        configuration = testConfiguration(DataFormat.Text);
        delegate = storageDelegate(configuration, world.defaultLogger());

        assertNotNull(delegate);

        final State.TextState writeState = new State.TextState("123", Entity1.class, 1, "{ \"data\" : \"data1\" }", 1, Metadata.with("metadata", "op"));

        delegate.beginWrite();
        final PreparedStatement writeStatement = delegate.writeExpressionFor(entity1StoreName, writeState);
        writeStatement.executeUpdate();
        delegate.complete();

        delegate.beginRead();
        final PreparedStatement readStatement = delegate.readExpressionFor(entity1StoreName, "123");
        final ResultSet result = readStatement.executeQuery();
        final State.TextState readState = delegate.stateFrom(result, "123");
        delegate.complete();

        assertEquals(writeState, readState);
    }

    @Test
    public void testThatTextStatesUpdate() throws Exception {
        configuration = testConfiguration(DataFormat.Text);
        delegate = storageDelegate(configuration, world.defaultLogger());

        assertNotNull(delegate);

        final State.TextState writeState1 = new State.TextState("123", Entity1.class, 1, "{ \"data\" : \"data1\" }", 1, Metadata.with("metadata1", "op1"));

        delegate.beginWrite();
        final PreparedStatement writeStatement1 = delegate.writeExpressionFor(entity1StoreName, writeState1);
        writeStatement1.executeUpdate();
        delegate.complete();

        delegate.beginRead();
        final PreparedStatement readStatement1 = delegate.readExpressionFor(entity1StoreName, "123");
        final ResultSet result1 = readStatement1.executeQuery();
        final State.TextState readState1 = delegate.stateFrom(result1, "123");
        delegate.complete();

        assertEquals(writeState1, readState1);

        final State.TextState writeState2 = new State.TextState("123", Entity1.class, 1, "{ \"data\" : \"data1\" }", 1, Metadata.with("metadata2", "op2"));

        delegate.beginWrite();
        final PreparedStatement writeStatement2 = delegate.writeExpressionFor(entity1StoreName, writeState2);
        writeStatement2.executeUpdate();
        delegate.complete();

        delegate.beginRead();
        final PreparedStatement readStatement2 = delegate.readExpressionFor(entity1StoreName, "123");
        final ResultSet result2 = readStatement2.executeQuery();
        final State.TextState readState2 = delegate.stateFrom(result2, "123");
        delegate.complete();

        assertEquals(writeState2, readState2);
        assertNotEquals(0, writeState1.compareTo(readState2));
        assertNotEquals(0, writeState2.compareTo(readState1));
    }

    @Test
    public void testThatBinaryWritesRead() throws Exception {
        configuration = testConfiguration(DataFormat.Binary);
        delegate = storageDelegate(configuration, world.defaultLogger());

        assertNotNull(delegate);

        final State.BinaryState writeState = new State.BinaryState("123", Entity1.class, 1, "{ \"data\" : \"data1\" }".getBytes(), 1, Metadata.with("metadata", "op"));

        delegate.beginWrite();
        final PreparedStatement writeStatement = delegate.writeExpressionFor(entity1StoreName, writeState);
        writeStatement.executeUpdate();
        delegate.complete();

        delegate.beginRead();
        final PreparedStatement readStatement = delegate.readExpressionFor(entity1StoreName, "123");
        final ResultSet result = readStatement.executeQuery();
        final State.BinaryState readState = delegate.stateFrom(result, "123");
        delegate.complete();

        assertEquals(writeState, readState);
    }

    @Before
    public void setUp() {
        world = World.startWithDefaults("test-store");

        entity1StoreName = Entity1.class.getSimpleName();
        StateTypeStateStoreMap.stateTypeToStoreName(Entity1.class, entity1StoreName);
    }

    @After
    public void tearDown() throws Exception {
        configuration.cleanUp();
        delegate.close();
        world.terminate();
    }

    /**
     * Create specific storage delegate.
     * @param configuration
     * @param logger
     * @return
     */
    protected abstract JDBCStorageDelegate<Object> storageDelegate(Configuration.TestConfiguration configuration, final Logger logger);

    /**
     * Create specific test configuration.
     * @param format
     * @return
     * @throws Exception
     */
    protected abstract Configuration.TestConfiguration testConfiguration(final DataFormat format) throws Exception;
}