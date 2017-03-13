package net.tirasa.connid.bundles.soap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.tirasa.connid.bundles.soap.utilities.Operand;
import net.tirasa.test.provisioningws.User;
import net.tirasa.test.provisioningws.UserService;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributeInfo;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;

@ConnectorClass(displayNameKey = "SOAP_CONNECTOR", configurationClass = WebServiceConfiguration.class)
public class WebServiceConnector implements
        PoolableConnector,
        SchemaOp,
        SearchOp<Operand>,
        TestOp {

    /**
     * Setup logging for the {@link WebServiceConnector}.
     */
    private static final Log LOG = Log.getLog(WebServiceConnector.class);

    /**
     * Place holder for the Connection created in the init method.
     */
    private WebServiceConnection connection;

    /**
     * Place holder for the {@link Configuration} passed into the init() method {@link WebServiceConnector#init}.
     */
    private WebServiceConfiguration config;

    /**
     * Schema.
     */
    private Schema schema = null;

    /**
     * Gets the Configuration context for this connector.
     *
     * @return
     */
    @Override
    public Configuration getConfiguration() {
        return config;
    }

    /**
     * Callback method to receive the {@link Configuration}.
     *
     * @param cfg connector configuration
     * @see Connector#init
     */
    @Override
    public void init(final Configuration cfg) {
        LOG.ok("Connector initialization");

        config = (WebServiceConfiguration) cfg;
        connection = new WebServiceConnection(config);
    }

    /**
     * Disposes of the {@link WebServiceConnector}'s resources.
     *
     * @see Connector#dispose()
     */
    @Override
    public void dispose() {
        LOG.ok("Dispose connector resources");

        config = null;

        if (connection != null) {
            connection.dispose();
            connection = null;
        }
        WebServiceConnection.shutdownBus();
    }

    /**
     * Checks if resource is alive.
     *
     * @see Connector#test()
     */
    @Override
    public void checkAlive() {
        LOG.ok("Connection test");

        connection.test();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Schema schema() {
        LOG.ok("Schema retrieving");

        AttributeInfoBuilder initialsAIB = new AttributeInfoBuilder("initials", String.class);
        initialsAIB.setRequired(true);

        Set<AttributeInfo> attrsInfo = new HashSet<AttributeInfo>();
        attrsInfo.add(initialsAIB.build());
        attrsInfo.add(AttributeInfoBuilder.build("firstname", String.class));
        attrsInfo.add(AttributeInfoBuilder.build("surname", String.class));
        attrsInfo.add(AttributeInfoBuilder.build("birthdate", String.class));

        ObjectClassInfo ociOrg = new ObjectClassInfoBuilder().setType(ObjectClass.ACCOUNT_NAME).
                addAllAttributeInfo(attrsInfo).
                build();

        SchemaBuilder schemaBld = new SchemaBuilder(getClass());
        schemaBld.defineObjectClass(ociOrg);

        schema = schemaBld.build();
        return schema;
    }

    /**
     * {@inheritDoc}
     *
     * @param oclass
     * @param options
     * @return
     */
    @Override
    public FilterTranslator<Operand> createFilterTranslator(
            final ObjectClass oclass,
            final OperationOptions options) {

        if (oclass == null || (!oclass.equals(ObjectClass.ACCOUNT))) {
            throw new IllegalArgumentException("Invalid objectclass");
        }

        return new WebServiceFilterTranslator();
    }

    /**
     * {@inheritDoc}
     *
     * @param objClass
     * @param options
     * @param query
     * @param handler
     */
    @Override
    public void executeQuery(
            final ObjectClass objClass,
            final Operand query,
            final ResultsHandler handler,
            final OperationOptions options) {

        if (LOG.isOk()) {
            LOG.ok("Execute query: " + query);
        }

        // check objectclass
        if (objClass == null || (!objClass.equals(ObjectClass.ACCOUNT))) {
            throw new IllegalArgumentException("Invalid objectclass");
        }

        // check handler
        if (handler == null) {
            throw new IllegalArgumentException("Invalid handler");
        }

        // get web service client
        final UserService provisioning = connection.getUserService();
        if (provisioning == null) {
            throw new IllegalStateException("Web Service client not found");
        }

        try {
            final List<User> resultSet = provisioning.getUsers();
            if (resultSet == null) {
                return;
            }

            boolean handle = true;
            for (final Iterator<User> i = resultSet.iterator(); i.hasNext() && handle;) {
                final User user = i.next();

                if (LOG.isOk()) {
                    LOG.ok("Found user: {0}", user);
                }

                try {
                    handle = handler.handle(buildConnectorObject(user).build());
                    LOG.ok("Handle: {0}", handle);
                } catch (Exception e) {
                    LOG.error(e, "Error building connector object for {0}", user);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void test() {
        connection.test();
    }

    private ConnectorObjectBuilder buildConnectorObject(final User user) {
        ConnectorObjectBuilder bld = new ConnectorObjectBuilder();
        bld.setObjectClass(ObjectClass.ACCOUNT);
        bld.setName(user.getInitials());
        bld.setUid(new Uid(user.getInitials()));

        bld.addAttribute(AttributeBuilder.build("firstname", user.getFirstname()));
        bld.addAttribute(AttributeBuilder.build("surname", user.getSurname()));
        bld.addAttribute(AttributeBuilder.build("birthdate", user.getBirthdate()));

        return bld;
    }
}
