// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.symbio.store.state.jdbc.yugabyte;

import io.vlingo.actors.Logger;
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.common.jdbc.yugabyte.YugaByteConfigurationProvider;
import io.vlingo.symbio.store.state.jdbc.JDBCStorageDelegate;
import io.vlingo.symbio.store.state.jdbc.JDBCStorageDelegateTest;
import org.junit.Ignore;

@Ignore
public class YugaByteStorageDelegateTest extends JDBCStorageDelegateTest {
    @Override
    protected JDBCStorageDelegate<Object> storageDelegate(Configuration.TestConfiguration configuration, Logger logger) {
        return new YugaByteStorageDelegate(configuration, logger);
    }

    @Override
    protected Configuration.TestConfiguration testConfiguration(DataFormat format) throws Exception {
        return YugaByteConfigurationProvider.testConfiguration(format);
    }
}