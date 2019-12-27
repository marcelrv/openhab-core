/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.core.thing.testutil.i18n;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Utility class for setting the default locale in tests.
 *
 * @author Henning Sudbrock - Initial contribution
 */
public class DefaultLocaleSetter {

    private final ConfigurationAdmin configAdmin;

    public DefaultLocaleSetter(ConfigurationAdmin configAdmin) {
        requireNonNull(configAdmin);
        this.configAdmin = configAdmin;
    }

    /**
     * Configures the i18n provider based on the provided locale. Note that the configuration is not necessarily
     * effective yet when this method returns, as the configuration admin might configure the i18n provider in another
     * thread.
     *
     * @param locale the locale to use, must not be null.
     */
    public void setDefaultLocale(Locale locale) throws IOException {
        assertThat(locale, is(notNullValue()));

        Configuration config = configAdmin.getConfiguration("org.eclipse.smarthome.i18n", null);
        assertThat(config, is(notNullValue()));

        Dictionary<String, Object> properties = config.getProperties();
        if (properties == null) {
            properties = new Hashtable<>();
        }

        properties.put("language", locale.getLanguage());
        properties.put("script", locale.getScript());
        properties.put("region", locale.getCountry());
        properties.put("variant", locale.getVariant());

        config.update(properties);
    }

}